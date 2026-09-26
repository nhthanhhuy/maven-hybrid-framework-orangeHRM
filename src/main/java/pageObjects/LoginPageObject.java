package pageObjects;

import core.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import pageUIs.LoginPageUI;

public class LoginPageObject extends BasePage {

    private WebDriver driver;

    public LoginPageObject(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    @Step("Enter username: {0}")
    public void enterUsernameTextbox(String username) {
        sendKeysToElement(LoginPageUI.USERNAME_TEXTBOX, username);
    }

    @Step("Enter password: {0}")
    public void enterPasswordTextbox(String password) {
        sendKeysToElement(LoginPageUI.PASSWORD_TEXTBOX, password);
    }

    @Step("Click to login button")
    public DashboardPageObject clickToLoginButton() {
        clickToElement(LoginPageUI.LOGIN_BUTTON);
        waitForLoadingIconInvisible();
        return PageGenerator.getDashboardPage(driver);
    }

}
