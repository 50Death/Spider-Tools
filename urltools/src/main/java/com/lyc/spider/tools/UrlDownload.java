package com.lyc.spider.tools;

import org.jsoup.Connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

public class UrlDownload {

    private HttpURL urls = new HttpURL();//储存待下载的URL

    private String path;//下载到的位置

    private String host;//代理地址
    private Vector<String> hosts = new Vector<String>();//代理地址池
    private int port;//代理端口号
    private Vector<String> ports = new Vector<String>();//代理端口池
    private int retry = 0;//重试次数

    private int threadNumber = 10;//设置下载线程数

    private boolean useProxy = false;//是否使用代理
    private boolean useProxyPool = false;//是否使用代理池

    private final Object lock = new Object();//代理池读取锁

    /**
     * 添加一个URL
     *
     * @param url
     */
    public void addURL(String url) {
        this.urls.addURL(url);
    }

    /**
     * 添加Vector
     *
     * @param urls
     */
    public void addURL(Vector<String> urls) {
        this.urls.addURL(urls);
    }

    /**
     * 添加一个HttpURL类
     *
     * @param httpURL
     */
    public void addURL(HttpURL httpURL) {
        this.urls.addURL(httpURL);
    }

    /**
     * 设置下载路径，没有会创建
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 添加一个代理
     *
     * @param host
     * @param port
     */
    public void setProxy(String host, int port) {
        this.host = host;
        this.port = port;
        this.useProxy = true;
    }

    /**
     * 设置重试次数
     *
     * @param retry
     */
    public void setRetry(int retry) {
        this.retry = retry;
    }

    /**
     * 设置下载线程数
     *
     * @param threads
     */
    public void setThread(int threads) {
        this.threadNumber = threads;
    }

    /**
     * 添加很多个代理,需满足格式127.0.0.1:8080
     *
     * @param proxy
     */
    public void setProxy(Vector<String> proxy) {
        for (String s : proxy) {
            String[] temp = s.split(":");
            this.hosts.add(temp[0]);
            this.ports.add(temp[1]);
        }
        this.useProxy = true;
    }

    /**
     * 设置是否使用代理池
     *
     * @param useProxyPool
     */
    public void useProxyPool(boolean useProxyPool) {
        this.useProxyPool = useProxyPool;
    }

    /**
     * 执行下载
     */
    public void execute() {
        //开启下载线程
        Thread[] t = new Thread[threadNumber];
        for (int i = 0; i < threadNumber; i++) {
            t[i] = new Thread(new Download());
            t[i].start();
        }

        //等待下载结束
        for (int i = 0; i < threadNumber; i++) {
            try {
                t[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class Download implements Runnable {
        public void run() {

            String url = null;

            while (urls.getSize() != 0 && (url = urls.popURL()) != null) {

                //下载失败换代理循环
                while (true) {

                    try {

                        //设置代理
                        if (useProxy) {
                            if (useProxyPool) {
                                //同步防止脏读
                                synchronized (lock) {
                                    System.setProperty("proxyHost", hosts.firstElement());
                                    System.setProperty("proxyPort", ports.firstElement());
                                }
                            } else {
                                System.setProperty("proxyHost", host);
                                System.setProperty("proxyPort", String.valueOf(port));
                            }
                        }
                        for (int i = -1; i < retry; i++) {

                            //下载
                            String fileName = url.substring(url.lastIndexOf("/") + 1);

                            //判断文件名是否过长
                            if (fileName.length() > 100) {
                                fileName = fileName.substring(fileName.length() - 100);
                            }

                            //文件名正则筛选，去掉不合法的字符
                            fileName = fileName.replaceAll("[^a-zA-Z0-9.]", "");

                            try {
                                File file = new File(path, fileName);
                                File parent = new File(path);

                                //如果下载路径不存在则建立
                                if (!parent.exists()) {
                                    parent.mkdirs();
                                }


                                URL dURL = new URL(url);

                                InputStream in = dURL.openStream();
                                FileOutputStream fos = new FileOutputStream(file);
                                byte[] buff = new byte[1024];
                                int len = 0;
                                while ((len = in.read(buff, 0, buff.length)) != -1) {
                                    fos.write(buff, 0, len);
                                }

                                in.close();
                                fos.close();
                                System.out.println("Download Success!");
                                break;

                            } catch (java.net.MalformedURLException e1) {
                                e1.printStackTrace();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        break;
                    } catch (Exception e) {
                        System.err.println("代理连接超时...删除并更换代理");
                        synchronized (lock) {
                            hosts.removeElementAt(0);
                            ports.removeElementAt(0);
                        }
                        continue;
                    }
                }
            }
        }
    }
}
