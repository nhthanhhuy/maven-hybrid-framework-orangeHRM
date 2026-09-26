package core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;
import reportConfig.AllureListener;

import java.io.File;
import java.time.Duration;

public class BaseTest {

    private WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

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

    @BeforeSuite
    public void cleanAllureResults() {
        File allureResultsDir = new File("target/allure-results");
        if (allureResultsDir.exists()) {
            for (File file : allureResultsDir.listFiles()) {
                file.delete();
            }
            log.info("Deleted old allure-results files");
        }
    }

// ========================= VERIFY =========================

    protected boolean verifyTrue(boolean condition) {
        boolean status = true;
        try {
            Assert.assertTrue(condition);
            log.info("---------------------- PASSED -----------------------");
        } catch (Throwable e) {
            status = false;
            log.error("---------------------- FAILED -----------------------");

            // Chụp screenshot ngay tại chỗ fail
            AllureListener.saveScreenshotPNG("Verify_fail", driver);

            // Attach log vào Allure
            AllureListener.saveTextLog("verifyTrue FAILED - Condition is false");

            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return status;
    }

    protected boolean verifyFalse(boolean condition) {
        boolean status = true;
        try {
            Assert.assertFalse(condition);
            log.info("---------------------- PASSED -----------------------");
        } catch (Throwable e) {
            status = false;
            log.error("---------------------- FAILED -----------------------");

            // Chụp screenshot ngay tại chỗ fail
            AllureListener.saveScreenshotPNG("Verify_fail", driver);

            // Attach log vào Allure
            AllureListener.saveTextLog("verifyFalse FAILED - Condition is true");

            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return status;
    }

    protected boolean verifyEquals(Object actual, Object expected) {
        boolean status = true;
        try {
            Assert.assertEquals(actual, expected);
            log.info("---------------------- PASSED -----------------------");
        } catch (Throwable e) {
            status = false;
            log.error("---------------------- FAILED -----------------------");
            log.error("Expected: " + expected);
            log.error("Actual  : " + actual);

            // Chụp screenshot ngay tại chỗ fail
            AllureListener.saveScreenshotPNG("Verify_fail", driver);

            // Attach log Expected/Actual vào Allure
            AllureListener.saveTextLog("verifyEquals FAILED" + "\nExpected: " + expected + "\nActual  : " + actual);

            VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
            Reporter.getCurrentTestResult().setThrowable(e);
        }
        return status;
    }

}
