import com.lyc.spider.tools.*;
import org.jsoup.nodes.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;

public class Demo {
    public static void main(String[] args){


        IPoolInitializer pool = new IPoolInitializer();
        pool.setDefaultHeaders();
        pool.setRetry(1);
        //pool.setProxy("110.52.235.77",9999);
        pool.setTimeout(10000);
        pool.setRandomSleep(2);
        //pool.initialize(500,true);
        //pool.setFilterPath("F:\\JAVA\\Crawl\\urltools\\OriginalProxy_2915882.txt");
        //pool.readPoolFromFile("F:\\JAVA\\Crawl\\urltools\\OriginalProxy_60074319.txt");
        pool.readPoolFromFile("F:\\JAVA\\Crawl\\urltools\\FilteredProxy_18633415.txt");
        //pool.filter(5000,3,1,500,false,true);

        IPoolInitializer pool2 = new IPoolInitializer();
        pool2.setDefaultHeaders();
        pool2.setRetry(1);
        int conter = 1;
        while(true) {
            try {
                System.out.println("Using Proxy NO."+conter+"  All:"+pool.getSize());
                conter++;
                pool2.setProxy(pool.getIP(), pool.getPort());
                pool2.setTimeout(10000);
                pool2.initialize(500,true);
            }catch (Exception e){
                e.printStackTrace();
            }
            pool2.filter(5000,3,1,500,false,true);
        }

        /**
         * 中断时间每页小于1
         * 67页 40秒BAN
         * 62页 43秒BAN
         * 68页 43秒BAN
         *
         * 无中断
         * 53页 13秒BAN
         * 63页  9秒BAN
         *
         * 2~3秒中断
         * 67页 3分7秒
         *
         * 目前推断：逐页访问60多页会被BAN
         *
         * TODO，随机数爬取，不同HTTP请求头爬取
         */



        /*
        IPool iPool = new IPool();
        iPool.initialize(200);
        iPool.filter(3000,2,2,500);
        for(int i=0;i<iPool.getSize();i++){
            System.out.println(iPool.getIP());
        }
        */
    }


}
