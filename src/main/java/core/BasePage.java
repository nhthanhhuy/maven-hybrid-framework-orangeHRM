package core;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BasePage {


    // ========================= DRIVER =========================

    protected void openUrl(WebDriver driver, String url) {
        driver.get(url);
    }

    protected String getCurrentUrl(WebDriver driver) {
        return driver.getCurrentUrl();
    }

    protected void navigateBack(WebDriver driver) {
        driver.navigate().back();
    }

    // ========================= LOCATOR =========================

    private String getDynamicLocator(String locator, String... values) {
        return String.format(locator, values);
    }

    private By getByLocator(String locator) {
        By by = null;
        if (locator.startsWith("ID=") || locator.startsWith("Id=") || locator.startsWith("id=")) {
            by = By.id(locator.substring(3));
        } else if (locator.startsWith("CLASS=") || locator.startsWith("Class=") || locator.startsWith("class=")) {
            by = By.className(locator.substring(6));
        } else if (locator.startsWith("NAME=") || locator.startsWith("Name=") || locator.startsWith("name=")) {
            by = By.name(locator.substring(5));
        } else if (locator.startsWith("TAGNAME=") || locator.startsWith("Tagname=") || locator.startsWith("tagname=")) {
            by = By.tagName(locator.substring(8));
        } else if (locator.startsWith("CSS=") || locator.startsWith("Css=") || locator.startsWith("css=")) {
            by = By.cssSelector(locator.substring(4));
        } else if (locator.startsWith("XPATH=") || locator.startsWith("Xpath=") || locator.startsWith("xpath=")) {
            by = By.xpath(locator.substring(6));
        } else {
            throw new RuntimeException("Locator type is not supported");
        }
        return by;
    }

    private WebElement getElement(WebDriver driver, String locator) {
        return driver.findElement(getByLocator(locator));
    }

    private List<WebElement> getListElement(WebDriver driver, String locator) {
        return driver.findElements(getByLocator(locator));
    }


    // ========================= WEB ELEMENT =========================

    protected void clickToElement(WebDriver driver, String locator) {
        waitForElementClickable(driver, locator);
        getElement(driver, locator).click();
    }

    protected void clickToElement(WebDriver driver, String locator, String... values) {
        waitForElementClickable(driver, getDynamicLocator(locator, values));
        getElement(driver, getDynamicLocator(locator, values)).click();
    }

    protected void sendKeysToElement(WebDriver driver, String locator, String value) {
        waitForElementVisible(driver, locator);
        getElement(driver, locator).clear();
        getElement(driver, locator).sendKeys(value);
    }

    protected void selectItemInDropDown(WebDriver driver, String locator, String itemText) {
        new Select(getElement(driver, locator)).selectByVisibleText(itemText);
    }

    protected String getSelectedItemInDropdown(WebDriver driver, String locator) {
        return new Select(getElement(driver, locator)).getFirstSelectedOption().getText();
    }

    protected boolean isDropdownMultiple(WebDriver driver, String locator) {
        return new Select(getElement(driver, locator)).isMultiple();
    }

    protected void selectItemInCustomDropdown(WebDriver driver, String parentLocator, String childItemLocator, String expectedItem) {
        getElement(driver, parentLocator).click();
        sleepInSeconds(2);

        List<WebElement> allItems = new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByLocator(childItemLocator)));

        for (WebElement item : allItems) {
            if (item.getText().trim().equals(expectedItem)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", item);
                sleepInSeconds(2);
                item.click();
                sleepInSeconds(2);
                break;
            }
        }
    }

    protected String getAttributeValue(WebDriver driver, String locator, String attributeName) {
        return getElement(driver, locator).getAttribute(attributeName);
    }

    protected String getAttributeValue(WebDriver driver, String locator, String attributeName, String restParameter) {
        return getElement(driver, getDynamicLocator(locator, restParameter)).getAttribute(attributeName);
    }

    protected Dimension getSizeElement(WebDriver driver, String locator) {
        return getElement(driver, locator).getSize();
    }

    protected String getTextElement(WebDriver driver, String locator) {
        return getElement(driver, locator).getText();
    }

    protected String getCssValue(WebDriver driver, String locator, String cssName) {
        return getElement(driver, locator).getCssValue(cssName);
    }

    protected String getHexaColorFromRGBA(String rgbaValue) {
        return Color.fromString(rgbaValue).asHex().toUpperCase();
    }

    protected int getElementsSize(WebDriver driver, String locator) {
        return getListElement(driver, locator).size();
    }

    protected void checkTheCheckboxOrRadio(WebDriver driver, String locator) {
        if (!getElement(driver, locator).isSelected()) {
            getElement(driver,locator).click();
        }
    }

    protected void checkTheCheckboxOrRadio(WebDriver driver, String locator, String... restParameter) {
        if (!getElement(driver, getDynamicLocator(locator, restParameter)).isSelected()) {
            getElement(driver,getDynamicLocator(locator, restParameter)).click();
        }
    }

    protected void uncheckTheCheckbox(WebDriver driver, String locator) {
        if (getElement(driver, locator).isSelected()) {
            getElement(driver, locator).click();
        }
    }

    protected void uncheckTheCheckbox(WebDriver driver, String locator, String... restParameter) {
        if (getElement(driver, getDynamicLocator(locator, restParameter)).isSelected()) {
            getElement(driver, getDynamicLocator(locator, restParameter)).click();
        }
    }

    protected boolean isDisplayed(WebDriver driver, String locator) {
        return getElement(driver, locator).isDisplayed();
    }

    protected boolean isDisplayed(WebDriver driver, String locator, String... restParameter) {
        return getElement(driver, getDynamicLocator(locator, restParameter)).isDisplayed();
    }

    protected boolean isSelected(WebDriver driver, String locator) {
        return getElement(driver, locator).isSelected();
    }

    protected boolean isSelected(WebDriver driver, String locator, String... restParameter) {
        return getElement(driver, getDynamicLocator(locator, restParameter)).isSelected();
    }

    protected boolean isEnabled(WebDriver driver, String locator) {
        return getElement(driver, locator).isEnabled();
    }

    protected void switchToIframe(WebDriver driver, String locator) {
        driver.switchTo().frame(getElement(driver, locator));
    }

    protected void switchToDefaultContent(WebDriver driver) {
        driver.switchTo().defaultContent();
    }

    protected Actions getActions(WebDriver driver) {
        return new Actions(driver);
    }

    protected void leftClickToElement(WebDriver driver, String locator) {
        getActions(driver).click(getElement(driver, locator)).perform();
    }

    protected void doubleClickToElement(WebDriver driver, String locator) {
        getActions(driver).doubleClick(getElement(driver, locator)).perform();
    }

    protected void hoverMouseToElement(WebDriver driver, String locator) {
        getActions(driver).moveToElement(getElement(driver, locator)).perform();
    }

    protected void rightClickToElement(WebDriver driver, String locator) {
        getActions(driver).contextClick(getElement(driver, locator)).perform();
    }

    protected void dragAndDropToElement(WebDriver driver, String sourceLocator, String targetLocator) {
        getActions(driver).dragAndDrop(getElement(driver, sourceLocator), getElement(driver, targetLocator)).perform();
    }

    protected void scrollToElement(WebDriver driver, String locator) {
        getActions(driver).scrollToElement(getElement(driver, locator)).perform();
    }

    protected void sendKeyboardToElement(WebDriver driver, String locator, Keys key) {
        getActions(driver).sendKeys(getElement(driver, locator), key).perform();
    }

    protected void sendKeyboardToElement(WebDriver driver, String locator, Keys key, String... restParameter) {
        getActions(driver).sendKeys(getElement(driver, getDynamicLocator(locator, restParameter)), key).perform();
    }

    protected void highlightElement(WebDriver driver, String locator) {
        WebElement element = getElement(driver, locator);
        String originalStyle = element.getAttribute("style");
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style', arguments[1])", element, "border: 2px solid red; border-style: dashed;");
        sleepInSeconds(2);
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style', arguments[1])", element, originalStyle);
    }

    protected void clickToElementByJS(WebDriver driver, String locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", getElement(driver, locator));
        sleepInSeconds(3);
    }

    protected void scrollToElementOnTopByJS(WebDriver driver, String locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getElement(driver, locator));
    }

    protected void scrollToElementOnDownByJS(WebDriver driver, String locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", getElement(driver, locator));
    }

    protected void scrollToBottomPageByJS(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    protected void setAttributeInDOM(WebDriver driver, String locator, String attributeName, String attributeValue) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('" + attributeName + "', '" + attributeValue + "');", getElement(driver, locator));
    }

    protected void removeAttributeInDOM(WebDriver driver, String locator, String attributeRemove) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].removeAttribute('" + attributeRemove + "');", getElement(driver, locator));
    }

    protected void sendkeyToElementByJS(WebDriver driver, String locator, String value) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('value', '" + value + "')", getElement(driver, locator));
    }

    protected String getAttributeInDOMByJS(WebDriver driver, String locator, String attributeName) {
        return (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].getAttribute('" + attributeName + "');", getElement(driver, locator));
    }

    protected String getElementValidationMessage(WebDriver driver, String locator) {
        return (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].validationMessage;", getElement(driver, locator));
    }

    protected boolean isImageLoaded(WebDriver driver, String locator) {
        return (boolean) ((JavascriptExecutor) driver).executeScript("return arguments[0].complete " +
                        "&& typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0",
                getElement(driver, locator));
    }


    // ========================= WAIT =========================

    protected void sleepInSeconds(long timeout) {
        try {
            Thread.sleep(timeout * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void waitForElementClickable(WebDriver driver, String locator) {
        new WebDriverWait(driver, Duration.ofSeconds(GlobalConstants.LONG_TIMEOUT)).until(ExpectedConditions.elementToBeClickable(getByLocator(locator)));}

    protected void waitForElementVisible(WebDriver driver, String locator) {
        new WebDriverWait(driver, Duration.ofSeconds(GlobalConstants.LONG_TIMEOUT)).until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator)));
    }

}
