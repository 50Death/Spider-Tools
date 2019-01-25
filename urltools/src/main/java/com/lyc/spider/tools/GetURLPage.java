package com.lyc.spider.tools;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Java Bean
 * 抓取url的全部html代码, 使用Jsoup抓取
 */
public class GetURLPage {
    private String url;//待爬URL
    private Map<String, String> headers = new HashMap<String, String>();//储存HTTP连接头部Headers
    private int timeout = 10000;//超时时长（默认10秒）
    private int retry = 0;//连接失败时重试次数,默认为0不重连

    //设定代理
    private String host;
    private int port;

    private Document page;//用于存储爬到的html网页
    private String state;

    /**
     * 构造函数，只输入url的情况下无头部默认抓取
     *
     * @param url
     */
    public GetURLPage(String url) {
        this.url = url;
    }

    /**
     * 构造函数，输入url和header时以header内为HTTP头部发送请求
     *
     * @param url
     * @param headers
     */
    public GetURLPage(String url, Map<String, String> headers) {
        this.url = url;
        this.headers = headers;
    }

    /**
     * 构造函数 简便设置一个headers，用于快速添加Cookie
     *
     * @param url
     * @param id    normally equals "Cookie"
     * @param value normally equals [cookie]
     */
    public GetURLPage(String url, String id, String value) {
        this.url = url;
        this.headers.put(id, value);
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
     * 设定代理
     *
     * @param host
     * @param port
     */
    public void setProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 设定连接失败重试次数
     *
     * @param retry
     */
    public void setRetry(int retry) {
        this.retry = retry;
    }

    /**
     * 得到页面HTML代码
     *
     * @return org.jsoup.nodes.Document
     */
    public Document getPage() {
        //重试循环
        for(int i=-1;i<retry;i++) {
            try {
                //实例化Connection对象
                Connection connection = Jsoup.connect(url);

                //判断是否需要添加头部
                if (!this.headers.isEmpty()) {
                    connection.headers(headers);
                }

                //判断是否需要添加代理
                if (this.host != null) {
                    connection.proxy(host, port);
                }

                //得到页面
                page = connection.get();

                //得到页面后退出重试循环
                break;
            } catch (IOException e) {
                e.printStackTrace();
                page = null;
            }
        }
        return page;
    }

    /**
     * 设置默认的User-Agent （Google Chrome）
     */
    public void setDefaultAgent() {
        this.headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
        this.headers.put("Connection", "Keep-Alive");
        this.headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        this.headers.put("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
        this.headers.put("Accept-Encoding", "gzip, deflate, br");
        this.headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
    }

}
