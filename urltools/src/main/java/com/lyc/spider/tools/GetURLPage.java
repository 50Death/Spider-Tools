package com.lyc.spider.tools;

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

    private Document page;//用于存储爬到的html网页

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
     * @param url
     * @param id normally equals "Cookie"
     * @param value normally equals [cookie]
     */
    public GetURLPage(String url, String id, String value){
        this.url = url;
        this.headers.put(id,value);
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
     * 得到页面HTML代码
     *
     * @return org.jsoup.nodes.Document
     */
    public Document getPage() {
        try {
            //当header为空，即不含有添加header
            if (this.headers.isEmpty()) {
                page = Jsoup.connect(url).timeout(timeout).get();
            } else {
                page = Jsoup.connect(url).headers(headers).timeout(timeout).get();
            }
        } catch (IOException e) {
            e.printStackTrace();
            page = null;
        }
        return page;
    }

}
