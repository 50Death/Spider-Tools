package com.lyc.spider.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * 储存类，用于一一对应存放一个网页的：标题、网址、摘要, 【线程安全】
 * Storage for web page. Storing titles, urls and summary. synchronized
 */
public class WebPage {

    private static final String NULL_STATEMENT = "nuLL-nULl-NUll-NulL";//Vector summary里表示空的陈述
    private static final int ELEMENT_NUMBER = 3;

    private Vector<String> titles = new Vector<String>();//储存标题
    private Vector<String> urls = new Vector<String>();//储存网址
    private Vector<String> summary = new Vector<String>();//储存摘要

    private boolean useSummary = false;

    /**
     * 添加页面
     *
     * @param title   标题
     * @param url     网址
     * @param summary 摘要
     */
    public synchronized void addPage(String title, String url, String summary) {
        this.titles.add(title);
        this.urls.add(url);
        this.summary.add(summary);
        useSummary = true;
    }

    /**
     * 添加页面
     *
     * @param title 标题
     * @param url   网址
     */
    public synchronized void addPage(String title, String url) {
        this.titles.add(title);
        this.urls.add(url);
        this.summary.add(NULL_STATEMENT);
    }

    /**
     * 得到最后一个页面，并删掉它
     *
     * @return String[] 0=title 1=urls 2=summary
     */
    public synchronized String[] popPage() {
        String[] page = new String[ELEMENT_NUMBER];
        page[0] = titles.lastElement();
        page[1] = urls.lastElement();
        if (useSummary) {
            page[2] = summary.lastElement();
            summary.remove(summary.lastElement());
        }

        titles.remove(titles.lastElement());
        urls.remove(urls.lastElement());

        return page;
    }

    /**
     * 得到最后一个页面，并保留它
     *
     * @return
     */
    public synchronized String[] getPage() {
        String[] page = new String[ELEMENT_NUMBER];
        page[0] = titles.lastElement();
        page[1] = urls.lastElement();
        if (useSummary) {
            page[2] = summary.lastElement();
        }

        return page;
    }

    /**
     * 得到所有页面，并保留
     *
     * @return
     */
    public synchronized String[][] getAllPage() {
        String[][] page = new String[titles.size()][ELEMENT_NUMBER];
        for (int i = 0; i < titles.size(); i++) {
            page[i][0] = titles.get(i);
            page[i][1] = urls.get(i);
            if (useSummary) {
                page[i][2] = summary.get(i);
            }
        }

        return page;
    }
}
