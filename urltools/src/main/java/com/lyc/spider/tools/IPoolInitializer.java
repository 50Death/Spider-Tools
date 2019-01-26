package com.lyc.spider.tools;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;

public class IPoolInitializer extends IPool {

    private static final String IP_SRC = "https://www.xicidaili.com/nn/";//IP池来源

    private int indexOfVerify = 0;
    private final Object verifyLock = new Object();

    private Map<String, String> headers = new HashMap<String, String>();//储存HTTP Headers
    private int timeout = 10000;//超时时间（默认为10s）
    private int retry = 0;//连接失败重新尝试次数，默认为0不重连
    private int getProxyThread = 1;//爬页面线程数
    private int verifyThread = 50;//测试代理线程数

    private String path;//筛选器筛选本地txt文件

    private int sleep = 0;//设置爬取一页后暂停时间

    private String host;//设置代理服务器地址
    private int port;//设置代理服务器端口号

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setDefaultHeaders() {
        this.headers = DefaultHeaders.getHeaders();
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public void setProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void setRandomSleep(int sleep) {
        this.sleep = sleep;
    }

    public void setFilterPath(String path) {
        this.path = path;
    }

    /**
     * 初始化，对西刺代理执行爬取，不要使用固定IP地址
     *
     * 输出在主目录下，OriginalProxy_[Random Number].txt表示初始爬取数据（IP未经验证能否使用）
     * FilteredProxy_[Time]_[Random].txt表示筛选后的数据，中间日期为输出日期
     *
     * 因为此类网站封IP迅速，所以单线程爬取
     * Since the sites like this banned spider real quick, there's no need to use multiple threads.
     *
     * @param pages
     * @param writeTXT 是否输出文档，不输出会造成结果丢失
     */
    public void initialize(int pages, boolean writeTXT) {

        System.out.println("Start Time :" + new Date());

        for (int i = 1; i <= pages; i++) {
            //得到页面
            GetURLPage urlPage = new GetURLPage(IP_SRC + i);
            urlPage.setDefaultAgent();
            urlPage.setTimeout(timeout);
            urlPage.setRetry(retry);
            if (host != null) {
                urlPage.setProxy(host, port);
            }
            Document doc = urlPage.getPage();

            try {
                //进行筛选
                Elements elements = doc.select("tr[class]");
                for (Element section : elements) {
                    Elements es = section.select("td");


                    String ip = es.get(1).text();//第2个<td>是IP地址（第一个是国家，不获取）
                    String port = es.get(2).text();//第3个是端口号
                    String location = es.get(3).select("a").text();//第四个是服务器地址
                    String type = es.get(5).text();//第六个是类型（第五个是否匿名不获取）
                    String speed = es.get(6).select("div[title]").attr("title").replace("秒", "");//第七个是速度，去掉 秒 这个字
                    String time = es.get(7).select("div[title]").attr("title").replace("秒", "");//第八个是连接时间，去掉秒
                    String life = es.get(8).text();//第九个是连接时间
                    String validate = es.get(9).text();//第十个是验证时间

                    IPModel ipmod = new IPModel();
                    ipmod.addIP(ip, Integer.parseInt(port), location, type, Double.parseDouble(speed), Double.parseDouble(time), life, validate);

                    ipool.add(ipmod);
                }
            } catch (NullPointerException e) {
                System.err.println("您已被www.xicidaili.com/nn/封禁\nYou have been BANNED from the server");
                e.printStackTrace();
                break;
            }

            System.out.println("拿到页数：" + i + "---" + new Date());


            int sleepTime = (int) (sleep + Math.random() * 2);//生成一个随机数用于爬完等待
            try {
                Thread.sleep(sleepTime * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        if (writeTXT) {
            try {

                PrintWriter pw = new PrintWriter("Original" +
                        "Proxy_" + (int) (Math.random() * 100000000) + ".txt");
                BufferedWriter bw = new BufferedWriter(pw);

                for (IPModel ip : ipool) {
                    String str = ip.toString();
                    bw.write(str + '\n');
                    bw.flush();
                }

                bw.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 筛选器，筛选Initialize生成的（存在内存中）或本地读取的txt
     * @param timeout 设置超时时长
     * @param retry 重试次数
     * @param maxTime 连接时间最大值
     * @param threadsNumber 线程数，推荐500以上
     * @param fromFile 是否是读取文件，为true时不再读取内存中init得到的结果
     * @param writeTXT 是否输出文件
     *                 FilteredProxy_[Time]_[Random].txt表示筛选后的数据，中间日期为输出日期
     */
    public void filter(int timeout, int retry, int maxTime, int threadsNumber, boolean fromFile, boolean writeTXT) {

        Vector<IPModel> ipool;

        if (fromFile) {

            ipool = new Vector<IPModel>();

            ipool.addAll(readPoolFromFile(path));

            super.ipool = ipool;

        } else {
            ipool = super.ipool;
        }

        //判断是否能用，能连通
        Thread[] threads = new Thread[threadsNumber];
        for (int i = 0; i < threadsNumber; i++) {
            threads[i] = new Thread(new VerifyConnection(timeout, retry));
            threads[i].start();
        }

        //等待联通测试结束
        for (int i = 0; i < threadsNumber; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Iterator<IPModel> iterator = super.ipool.iterator();

        while (iterator.hasNext()) {
            IPModel ipm = iterator.next();
            if (!ipm.type.equals("HTTPS")) {//如果它不是HTTPS类型，删除掉
                iterator.remove();
                continue;
            }
            if (ipm.speed > maxTime || ipm.time > maxTime) {//连接时间或速度时间大于指定值maxTime时删除
                iterator.remove();
                continue;
            }
            if (!ipm.status) {//如果无法连通，删除掉
                iterator.remove();
                continue;
            }
        }

        //排序
        Collections.sort(super.ipool);

        if (writeTXT) {
            try {

                PrintWriter pw = new PrintWriter("Filtered" +
                        "Proxy_" +new Date()+"_"+ (int) (Math.random() * 100000000) + ".txt");
                BufferedWriter bw = new BufferedWriter(pw);

                for (IPModel ip : super.ipool) {
                    String str = ip.toString();
                    bw.write(str + '\n');
                    bw.flush();
                }

                bw.close();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 多线程验证IP是否可用
     */
    private class VerifyConnection implements Runnable {
        private int timeout;
        private int retry;

        VerifyConnection(int timeout, int retry) {
            this.timeout = timeout;
            this.retry = retry;
        }

        public void run() {
            while (true) {
                IPModel ipm;
                synchronized (verifyLock) {
                    if (indexOfVerify >= ipool.size()) {
                        break;
                    }
                    ipm = ipool.get(indexOfVerify);
                    System.out.println("Verifying [" + indexOfVerify + "/" + ipool.size() + "]");
                    indexOfVerify++;
                }
                if (!ipm.verify(timeout, retry)) {
                    ipool.removeElement(ipm);
                }
            }
        }
    }
}
