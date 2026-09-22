package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.v151.log.Log;

public class PageGenerator {

    public static LoginPageObject getLoginPage(WebDriver driver) {
        return new LoginPageObject(driver);
    }
    public static DashboardPageObject getDashboardPage(WebDriver driver) {
        return new DashboardPageObject(driver);
    }

}
