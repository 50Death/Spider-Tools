package com.lyc.spider.tools;


import java.util.Vector;

/**
 * 储存URL并线程安全的读取输出URL
 * URL Storage and output, synchronized
 */
public class HttpURL {

    private Vector<String> url = new Vector<String>();

    /**
     * 添加URL
     *
     * @param toAddURL
     */
    public synchronized void addURL(String toAddURL) {
        url.add(toAddURL);
    }

    /**
     * 添加多个URL，存储在Vector<String>里
     *
     * @param toAddURLs
     */
    public synchronized void addURL(Vector<String> toAddURLs) {
        url.addAll(toAddURLs);
    }

    /**
     * 获得最后一个URL，并删除它
     *
     * @return
     */
    public synchronized String popURL() {
        if (url.isEmpty()) {
            throw new NullPointerException();
        }
        String lastURL = url.lastElement();
        url.removeElement(lastURL);
        return lastURL;
    }

    /**
     * 获得最后一个URL，不删除它
     *
     * @return
     */
    public synchronized String getURL() {
        if (url.isEmpty()) {
            throw new NullPointerException();
        }
        String lastURL = url.lastElement();
        return lastURL;
    }

    /**
     * 获得所有URL，不删除内容
     *
     * @return
     */
    public synchronized Vector<String> getAllURL() {
        return url;
    }

    /**
     * 获得URL队列长度
     *
     * @return
     */
    public synchronized int getSize() {
        return url.size();
    }

    /**
     * 删除元素
     * @param element
     */
    public synchronized void deleteElement(String element){
        url.removeElement(element);
    }

    /**
     * 移除重复元素
     */
    public synchronized void removeDuplicate(){
        url = URLFetch.removeDuplicates(url);
    }
}
