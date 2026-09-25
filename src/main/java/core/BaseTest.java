package core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class BaseTest {

    private WebDriver driver;

    protected static final Logger log = LogManager.getLogger(BaseTest.class);

    protected WebDriver getBrowserDriver(String browserName) {

        switch (browserName.toUpperCase()) {
            case "CHROME":
                driver = new ChromeDriver();
                break;
            case "EDGE":
                driver = new EdgeDriver();
                break;
            case "FIREFOX":
                driver = new FirefoxDriver();
                break;
            default: throw new RuntimeException("The browser name is invalid");

        }

        log.info("===== START BROWSER: " + browserName + " =====");
        log.info("Opening URL: " + GlobalConstants.ORANGE_HRM_URL);
        driver.get(GlobalConstants.ORANGE_HRM_URL);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        return driver;
    }

    protected WebDriver getBrowserDriver(String browserName, String appURL) {

        switch (browserName.toUpperCase()) {
            case "CHROME":
                driver = new ChromeDriver();
                break;
            case "EDGE":
                driver = new EdgeDriver();
                break;
            case "FIREFOX":
                driver = new FirefoxDriver();
                break;
            default: throw new RuntimeException("The browser name is invalid");

        }
        log.info("===== START BROWSER: " + browserName + " =====");
        log.info("Opening URL: " + appURL);
        driver.get(appURL);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        return driver;
    }

}
