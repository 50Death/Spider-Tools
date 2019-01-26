import com.lyc.spider.slimapp.GoogleResult;
import com.lyc.spider.tools.HttpURL;
import com.lyc.spider.tools.UrlDownload;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) {

        GoogleResult google = new GoogleResult("");

        google.setProxy("127.0.0.1",1080);

        google.setPages(200);

        google.setThread(5);

        google.setRetry(5);

        google.setTimeout(15000);

        HttpURL urls = google.getUrls();

        UrlDownload dl = new UrlDownload();

        dl.addURL(urls);

        dl.setProxy("127.0.0.1",1080);

        dl.setRetry(3);

        dl.setPath("");

        dl.setThread(50);

        dl.execute();

    }
}
