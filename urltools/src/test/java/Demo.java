import com.lyc.spider.tools.DefaultHeaders;
import com.lyc.spider.tools.URLFetch;
import com.lyc.spider.tools.WebPage;

import java.util.Vector;

public class Demo {
    public static void main(String[] args){
        WebPage wp = new WebPage();
        wp.addPage("a","b","c");
        for(String[] s:wp.getAllPage()){
            System.out.println(s[0]);
            System.out.println(s[1]);
            System.out.println(s[2]);
        }
    }
}
