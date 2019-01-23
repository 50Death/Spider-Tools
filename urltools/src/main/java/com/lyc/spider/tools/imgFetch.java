package com.lyc.spider.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class imgFetch {
    private String url;//主URL，爬取该页面所有图片
    private Map<String, String> headers = new HashMap<String, String>();//储存HTTP请求头部
    private int timeout = 10000;//超时时间设置,默认10秒
    private int totalNumber = 100;//下载图片数量限制，默认100张
    private int threadNumber = 10;//下载图片线程数，默认10线程
    private int alreadyDL = 0;

    private HttpURL urls;//下载图片队列
    private String path;//下载存储路径

    /**
     * 若干个不同类型构造函数，用来设置爬取url,HTTP头部headers，下载路径path
     *
     * @param url
     */
    public imgFetch(String url) {
        this.url = url;
    }

    public imgFetch(String url, Map<String, String> headers) {
        this.url = url;
        this.headers = headers;
    }

    public imgFetch(String url, String path) {
        this.url = url;
        this.path = path;
    }

    public imgFetch(String url, String path, Map<String, String> headers) {
        this.url = url;
        this.path = path;
        this.headers = headers;
    }

    public imgFetch(String url, String path, String id,String value){
        this.url = url;
        this.path = path;
        headers.put(id,value);
    }

    /**
     * 设置下载路径
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 设置超时时间
     *
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * 设置下载数量
     *
     * @param number
     */
    public void setTotalNumber(int number) {
        this.totalNumber = number;
    }

    /**
     * 设置下载线程数
     *
     * @param thread
     */
    public void setThreadNumber(int thread) {
        this.threadNumber = thread;
    }

    /**
     * 开始下载入口，如果路径不存在将会新建
     */
    public void startDownload(){
        //创建抓取图像URL对象
        URLFetch urlFetch;

        //判断是否需要输入HTTP头部
        if(headers.isEmpty()){
            urlFetch = new URLFetch(url);
        }else {
            urlFetch = new URLFetch(url, headers);
        }

        //设置抓取模式，抓取img图像URL
        urlFetch.setMode(URLFetch.Modes.img);

        //得到URLs，存储在HttpURL里
        urls = urlFetch.getUrls();

        //创建下载线程，在队列HttpURL为空后自动关闭线程
        Thread[] dl = new Thread[threadNumber];
        for(int i=0;i<threadNumber;i++){
            dl[i] = new Thread(new DownloadIMG());
            dl[i].start();
        }
        try {
            //等待下载线程全部结束
            for(int i=0;i<threadNumber;i++){
                dl[i].join();
                //System.out.println(dl[i].getState());
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /**
     * 下载线程，多发，HttpURL为线程安全类，所以不用同步加锁
     */
    private class DownloadIMG implements Runnable{
        public void run() {

            //当队列为空时退出线程
            while ((urls.getSize()!=0)&&(alreadyDL<=totalNumber)){

                String url = urls.popURL();//获得一个下载链接，获得过程线程安全
                String fileName = url.substring(url.lastIndexOf("/")+1,url.length());//取最后一次出现/之后的为文件名

                //判断文件名是否过长
                if(fileName.length()>200){
                    //fileName = new String(fileName.substring(url.length()-100,url.length()-1));
                    int i= 0;
                }

                try{
                    File file = new File(path,fileName);
                    File parent = new File(path);

                    //如果输入下载路径不存在则建立路径文件夹
                    if(!parent.exists()){
                        parent.mkdirs();
                    }

                    //创建URL对象
                    URL dlURL = new URL(url);

                    //开启下载流
                    InputStream in = dlURL.openStream();
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buff = new byte[1024];
                    int len = 0;
                    while ((len = in.read(buff,0,buff.length))!=-1){
                        fos.write(buff,0,len);
                    }
                    in.close();
                    fos.close();

                    //统计下载数量
                    alreadyDL++;

                }catch (java.net.MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
