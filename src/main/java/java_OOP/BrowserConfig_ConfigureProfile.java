package java_OOP;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class BrowserConfig_ConfigureProfile {

    public static void main(String[] args) throws InterruptedException {


//        ChromeOptions chromeOptions = new ChromeOptions();
//        EdgeOptions edgeOptions = new EdgeOptions();

//        chromeOptions.addArguments("--user-data-dir=C:\\Users\\T14\\AppData\\Local\\Google\\Chrome\\User Data\\");
//        chromeOptions.addArguments("--profile-directory=Profile 5");
//        chromeOptions.addArguments("--no-sandbox");
//
//        chromeDriver = new ChromeDriver(chromeOptions);
//        chromeDriver.get("https://demo.nopcommerce.com/");
//        Thread.sleep(5000);
//        chromeDriver.quit();
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("--no-sandbox");
        edgeOptions.setBinary("C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe");
        edgeOptions.setBinary("C:\\Users\\T14\\AppData\\Local\\Microsoft\\Edge\\User Data\\Profile4");

//        edgeOptions.addArguments("--profile-directory=Profile4");

//        edgeOptions.setBinary("C:/Program Files (x86)/Microsoft/Edge/Application/msedge.exe");


        WebDriver edgeDriver = new EdgeDriver(edgeOptions);
        edgeDriver.get("https://demo.nopcommerce.com/");
        Thread.sleep(5000);
        edgeDriver.quit();
        System.out.println("DONE");


    }
}
