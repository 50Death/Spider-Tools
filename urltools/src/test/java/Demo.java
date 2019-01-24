import com.lyc.spider.tools.DefaultHeaders;
import com.lyc.spider.tools.URLFetch;

import java.util.Vector;

public class Demo {
    public static void main(String[] args){
        URLFetch urlFetch = new URLFetch("https://baidu.com", DefaultHeaders.getHeaders());
        urlFetch.setTimeout(30000);
        //urlFetch.setProxy("127.0.0.1",1080);
        urlFetch.setRetry(1);
        urlFetch.setMode(URLFetch.Modes.links);
        Vector<String> v = urlFetch.getUrlsVec();
        for(String s:v){
            System.out.println(s);
        }
    }
}
