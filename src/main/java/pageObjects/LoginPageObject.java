package pageObjects;

import core.BasePage;
import org.openqa.selenium.WebDriver;
import pageUIs.LoginPageUI;

public class LoginPageObject extends BasePage {

    private WebDriver driver;

    public LoginPageObject(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public void enterUsernameTextbox(String username) {
        sendKeysToElement(LoginPageUI.USERNAME_TEXTBOX, username);
    }

    public void enterPasswordTextbox(String password) {
        sendKeysToElement(LoginPageUI.PASSWORD_TEXTBOX, password);
    }

    public DashboardPageObject clickToLoginButton() {
        clickToElement(LoginPageUI.LOGIN_BUTTON);
        waitForLoadingIconInvisible();
        return PageGenerator.getDashboardPage(driver);
    }

}
