package java_OOP;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class BrowserConfig_Language {

    public static void main(String[] args) throws InterruptedException {
        WebDriver chromeDriver, edgeDriver;
        ChromeOptions chromeOptions = new ChromeOptions();
        EdgeOptions edgeOptions = new EdgeOptions();

        chromeOptions.addArguments("--lang=ja-JP");
        edgeOptions.addArguments("--lang=ja-JP");

        chromeDriver = new ChromeDriver(chromeOptions);
        Thread.sleep(5000);
        chromeDriver.quit();

        edgeDriver = new EdgeDriver(edgeOptions);
        Thread.sleep(5000);
        edgeDriver.quit();

    }
}
