package testcases.employee;

import core.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.DashboardPageObject;
import pageObjects.LoginPageObject;
import pageObjects.PageGenerator;


@Epic("Employee management")

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
    @Story("Admin log in to dashboard successfully")
    @Description("Verify admin can log in to dashboard")
    public void LoginToDashBoard_01() {
        log.info("STEP 1 - Enter username - " + username);
        loginPage.enterUsernameTextbox(username);

        log.info("STEP 2 - Enter password - " + password);
        loginPage.enterPasswordTextbox(password);

        log.info("STEP 3 - Click to log in button");
        log.info("STEP 4 - Wait for all loading icon disappear");
        dashboardPage = loginPage.clickToLoginButton();

    }

    @Story("Story - Test only")
    @Description("Description - Test only")
    @Test
    public void NewEmployee_02() {
        Assert.assertTrue(false);
    }

    @AfterClass
    public void afterClass() {
        driver.quit();
    }


}
