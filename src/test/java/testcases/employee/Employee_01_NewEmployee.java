package testcases.employee;

import core.BaseTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.DashboardPageObject;
import pageObjects.LoginPageObject;
import pageObjects.PageGenerator;

import java.time.Duration;

public class Employee_01_NewEmployee extends BaseTest {

    private WebDriver driver;
    private String username, password;
    private LoginPageObject loginPage;
    private DashboardPageObject dashboardPage;

    @Parameters({"browser",  "url"})
    @BeforeClass
    public void beforeClass(String browserName, String appURL) {

        driver = getBrowserDriver(browserName, appURL);
        username = "admin";
        password = "K25/t23{T--Y";
        loginPage = PageGenerator.getLoginPage(driver);
    }

    @Test
    public void LoginToDashBoard_01() {
        log.info("STEP 1 - Enter username - ", username);
        loginPage.enterUsernameTextbox(username);

        log.info("STEP 2 - Enter password - ", password);
        loginPage.enterPasswordTextbox(password);

        log.info("STEP 3 - Click to log in button");
        log.info("STEP 4 - Wait for all loading icon disappear");
        dashboardPage = loginPage.clickToLoginButton();

    }

    @Test
    public void NewEmployee_02() {

    }

    @AfterClass
    public void afterClass() {
        driver.quit();
    }


}
