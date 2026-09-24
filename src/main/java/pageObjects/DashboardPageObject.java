package pageObjects;

import core.BasePage;
import org.openqa.selenium.WebDriver;

public class DashboardPageObject extends BasePage {

    private WebDriver driver;

    public DashboardPageObject(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }
}
