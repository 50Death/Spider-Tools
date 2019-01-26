package com.lyc.spider.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * 爬取西刺代理的免费IP代理地址
 * 注意不要使用自己的固定IP爬取不然会被封很久
 * Warning not to use your own IP address to crawl this site or you will get banned for a long period of time.
 */

public class IPool {

    private static final String IP_SRC = "https://www.xicidaili.com/nn/";//IP池来源

    protected Vector<IPModel> ipool = new Vector<IPModel>();//存储IP

    //取用指针，每次挨个取，可循环
    private int indexOfIPModel = -1;
    private int indexOfIPAddress = -1;
    private int indexOfIPPort = -1;
    private int indexOfVerify = 0;
    //线程锁
    private Object verifyLock = new Object();

    //添加IPModel
    public void add(String ip, int port, String location, String type, double speed, double time, String life, String validate) {
        this.ipool.add(new IPModel(ip, port, location, type, speed, time, life, validate));
    }
    //直接添加IPModel
    public void add(IPModel ipool) {
        this.ipool.add(ipool);
    }

    /**
     * 得到一个IP的完整信息
     * @return
     */
    public IPModel getIPModel() {
        if (indexOfIPModel == ipool.size()) {
            indexOfIPModel = -1;
        }
        indexOfIPModel++;
        return ipool.get(indexOfIPModel);
    }

    /**
     * 得到一个IP地址，调用filter之后才能让此IP可用概率更高
     * @return
     */
    public String getIP() {
        if (indexOfIPAddress == ipool.size()) {
            indexOfIPAddress = -1;
        }
        indexOfIPAddress++;
        return ipool.get(indexOfIPAddress).ip;
    }

    /**
     * 得到端口号
     * @return
     */
    public int getPort() {
        if (indexOfIPPort == ipool.size()) {
            indexOfIPPort = -1;
        }
        indexOfIPPort++;
        return ipool.get(indexOfIPPort).port;
    }

    /**
     * 得到所有的代理服务器+端口,以如下格式发出： 127.0.0.1:8080
     * @return
     */
    public Vector<String> getAllProxy(){
        Vector v = new Vector();
        for(IPModel ipm: this.ipool){
            String ip = ipm.ip;
            String port = String.valueOf(ipm.port);
            String toAdd = ip+":"+port;
            v.add(toAdd);
        }
        return v;
    }

    /**
     * 得到大小，用于混合多线程爬取
     * @return
     */
    public int getSize(){
        return ipool.size();
    }

    /**
     * 读取TXT文件
     * @param path
     * @return
     */
    public Vector<IPModel> readPoolFromFile(String path){
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));//构造一个BufferedReader类来读取文件
            String section = null;
            while ((section = br.readLine()) != null) {//使用readLine方法，一次读一行
                String[] s = section.split(",");
                ipool.add(new IPModel(s[0], Integer.parseInt(s[1]), s[2], s[3], Double.parseDouble(s[4]), Double.parseDouble(s[5]), s[6], s[7], s[8]));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("TXT文件损坏");
        }

        return ipool;
    }

}
