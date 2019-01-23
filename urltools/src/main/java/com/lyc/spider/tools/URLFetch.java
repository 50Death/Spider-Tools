package com.lyc.spider.tools;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

public class URLFetch {
    private String url;//存储待爬URL
    private Map<String, String> headers = new HashMap<String, String>();//储存HTTP Headers
    private int timeout = 10000;//超时时间（默认为10s）

    public enum Modes {links, img}//TODO 设置一个枚举类型用来选择筛选条件

    private Modes mode = Modes.links;//筛选条件存放处, 默认为“可点击超链接”

    private Vector<String> urls = new Vector<String>();//储存结果

    /**
     * 构造函数 只设置url
     *
     * @param url
     */
    public URLFetch(String url) {
        this.url = url;
    }

    /**
     * 构造函数 设置URL和header
     *
     * @param url
     * @param headers
     */
    public URLFetch(String url, Map<String, String> headers) {
        this.url = url;
        this.headers = headers;
    }

    /**
     * 构造函数 简便设置一个头部如Cookie
     *
     * @param url
     * @param id    "Cookie"
     * @param value [cookie]
     */
    public URLFetch(String url, String id, String value) {
        this.url = url;
        this.headers.put(id, value);
    }

    /**
     * 设置超时时长，不设置默认为10秒
     *
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * TODO 设置筛选器，all=所有超链接, links=新的网址，img=图片网址
     *
     * @param mode
     */
    public void setMode(Modes mode) {
        this.mode = mode;
    }

    /**
     * 获得URL，主执行函数, 返回Vector
     *
     * @return
     */
    public HttpURL getUrls() {
        //创建获得网址页面对象
        GetURLPage urlPage;

        //判断是否需要填入header信息
        if (headers.isEmpty()) {
            urlPage = new GetURLPage(url);
        } else {
            urlPage = new GetURLPage(url, headers);
        }

        //填入超时设置
        urlPage.setTimeout(timeout);

        //得到页面
        Document page = urlPage.getPage();

        //根据筛选模式进行筛选
        this.urls = urlFilter(page, this.mode);

        //将结果存进HttpURL类里
        HttpURL httpURL = new HttpURL();
        httpURL.addURL(this.urls);

        //返回结果
        return httpURL;
    }

    /**
     * 获得Vector的URLs
     *
     * @return
     */
    public Vector<String> getUrlsVec() {
        if(this.urls.isEmpty()){
            getUrls();
        }
        return this.urls;
    }

    /**
     * URL筛选器 输入整个HTML文档，输出需要的URL，静态可随时调用
     *
     * @param page HTML文档
     * @param mode 筛选模式，links = 超链接；img = 图片链接
     * @return
     */
    public static Vector<String> urlFilter(Document page, Modes mode) {
        Vector<String> url = new Vector<String>();
        Elements links;
        if (mode == Modes.links) {
            links = page.select("a[href]");
            for (Element e : links) {
                String str = e.attr("abs:href");
                if (str.equals("")) {
                    continue;
                }
                url.add(str);
            }
        } else if (mode == Modes.img) {
            links = page.select("img[src]");
            for (Element e : links) {
                String str = e.attr("abs:src");
                if (str.equals("")) {
                    continue;
                }
                url.add(str);
            }
        }

        url = removeDuplicates(url);
        return url;
    }

    /**
     * 提供的网址去重操作方法，静态可直接使用
     *
     * @param urls
     * @return
     */
    public static Vector<String> removeDuplicates(Vector<String> urls) {
        HashSet<String> hashSet = new HashSet<String>(urls);
        Vector<String> result = new Vector<String>();
        result.addAll(hashSet);
        return result;
    }

    /**
     * TODO 暂时写在这里，以后封装成另一个工具类
     * 两个Vector去重,把b中的所有元素和a比较，去掉b在a中存在的所有元素，即保留b独有元素
     *
     * @param a
     * @param b
     * @return
     */
    public static Vector removeDuplicates(Vector a, Vector b) {
        Vector v = new Vector();
        v.addAll(b);

        for (Object o : v) {
            if (a.contains(o)) {
                v.removeElement(o);
            }
        }
        return v;
    }

    /**
     * 对this进行去重操作，期间不能进行读写操作
     */
    public void removeDuplicates() {
        HashSet<String> hashSet = new HashSet<String>(this.urls);
        this.urls.clear();
        this.urls.addAll(hashSet);
    }


}
