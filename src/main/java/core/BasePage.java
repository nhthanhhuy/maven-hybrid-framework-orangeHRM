package core;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageUIs.BasePageUI;

import java.time.Duration;
import java.util.List;

/**
 * BasePage — lớp nền cho tất cả PageObject trong framework.
 *
 * Mọi PageObject đều extends class này để tái sử dụng các hàm Selenium chung.
 * Không viết test logic ở đây — chỉ chứa các hàm wrapper cho Selenium WebDriver.
 *
 * Cấu trúc file (theo thứ tự từ trên xuống):
 *   1. BROWSER          — Điều hướng trình duyệt
 *   2. LOCATOR          — Các hàm private hỗ trợ parse và tìm element (không gọi trực tiếp từ PageObject)
 *   3. ELEMENT — GET    — Lấy thông tin từ element (text, attribute, size, color...)
 *   4. ELEMENT — STATE  — Kiểm tra trạng thái element (displayed, selected, enabled)
 *   5. ELEMENT — ACTION — Tương tác với element (click, type, checkbox, dropdown...)
 *   6. ACTIONS API      — Các thao tác nâng cao dùng Actions class (hover, drag & drop, keyboard...)
 *   7. JAVASCRIPT       — Các hàm thực thi JavaScript (click, scroll, DOM manipulation...)
 *   8. FRAME & WINDOW   — Xử lý iframe và switch context
 *   9. WAIT             — Explicit wait và sleep
 */
public class BasePage {

    private WebDriver driver;
    private WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(GlobalConstants.LONG_TIMEOUT));
    }



    // =============================================================================
    // 1. BROWSER
    // Các hàm điều hướng cơ bản của trình duyệt.
    // =============================================================================

    /**
     * Mở URL trên trình duyệt.
     * Dùng trong @BeforeClass hoặc khi cần navigate đến trang mới.
     */
    protected void openUrl(String url) {
        driver.get(url);
    }

    /**
     * Lấy URL hiện tại trên thanh địa chỉ.
     * Dùng để verify sau khi login hoặc redirect.
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Quay lại trang trước (tương đương nút Back trên trình duyệt).
     */
    protected void navigateBack() {
        driver.navigate().back();
    }


    // =============================================================================
    // 2. LOCATOR  (private — chỉ dùng nội bộ trong BasePage)
    // Các hàm hỗ trợ parse locator string thành By object.
    //
    // Quy ước đặt tên locator trong PageObject:
    //   private String loginButton = "xpath=//button[@type='submit']";
    //   private String menuItem    = "xpath=//li[text()='%s']";   // dynamic locator dùng %s
    // =============================================================================

    /**
     * Build dynamic locator bằng cách thay thế %s trong chuỗi XPath/CSS.
     *
     * Ví dụ:
     *   locator = "xpath=//li[text()='%s']"
     *   values  = "Admin"
     *   → "xpath=//li[text()='Admin']"
     */
    private String getDynamicLocator(String locator, String... values) {
        return String.format(locator, values);
    }

    /**
     * Parse prefix của locator string và trả về By object tương ứng.
     * Hỗ trợ: id=, class=, name=, tagname=, css=, xpath= (không phân biệt hoa thường).
     *
     * Ném RuntimeException nếu prefix không được hỗ trợ.
     */
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

    /**
     * Tìm và trả về một WebElement theo locator string.
     */
    private WebElement getElement(String locator) {
        return driver.findElement(getByLocator(locator));
    }

    /**
     * Tìm và trả về danh sách WebElement theo locator string.
     * Dùng khi cần đếm số lượng element hoặc loop qua nhiều element.
     */
    private List<WebElement> getListElement(String locator) {
        return driver.findElements(getByLocator(locator));
    }


    // =============================================================================
    // 3. ELEMENT — GET
    // Lấy thông tin từ element: text, attribute, CSS, size, màu sắc...
    // =============================================================================

    /**
     * Lấy text hiển thị của element.
     * Tương đương .getText() của Selenium.
     */
    protected String getTextElement(String locator) {
        waitForElementVisible(locator);
        return getElement(locator).getText();
    }

    /**
     * Lấy giá trị của một HTML attribute (vd: value, placeholder, href, class...).
     */
    protected String getAttributeValue(String locator, String attributeName) {
        waitForElementVisible(locator);
        return getElement(locator).getAttribute(attributeName);
    }

    /**
     * Overload — dùng khi locator là dynamic (có %s).
     */
    protected String getAttributeValue(String locator, String attributeName, String... restParameter) {
        waitForElementVisible(getDynamicLocator(locator, restParameter));
        return getElement(getDynamicLocator(locator, restParameter)).getAttribute(attributeName);
    }

    /**
     * Lấy giá trị CSS property của element (vd: color, background-color, font-size...).
     * Kết quả trả về dạng rgba — dùng getHexaColorFromRGBA() để convert sang HEX nếu cần.
     */
    protected String getCssValue(String locator, String cssName) {
        waitForElementVisible(locator);
        return getElement(locator).getCssValue(cssName);
    }

    /**
     * Convert màu từ định dạng rgba sang HEX (viết hoa).
     *
     * Ví dụ:
     *   rgba(0, 158, 96, 1) → "#009E60"
     *
     * Dùng kết hợp với getCssValue():
     *   String rgba = getCssValue(driver, locator, "color");
     *   String hex  = getHexaColorFromRGBA(rgba);
     */
    protected String getHexaColorFromRGBA(String rgbaValue) {
        return Color.fromString(rgbaValue).asHex().toUpperCase();
    }

    /**
     * Lấy kích thước (width x height) của element.
     * Dùng để verify UI layout hoặc responsive design.
     */
    protected Dimension getSizeElement(String locator) {
        return getElement(locator).getSize();
    }

    /**
     * Đếm số lượng element khớp với locator.
     * Dùng để verify số dòng trong table, số item trong list...
     */
    protected int getElementsSize(String locator) {
        return getListElement(locator).size();
    }


    // =============================================================================
    // 4. ELEMENT — STATE
    // Kiểm tra trạng thái của element: có hiển thị không, có được chọn không...
    // =============================================================================

    /**
     * Kiểm tra element có đang hiển thị trên UI không.
     * Lưu ý: element vẫn có thể tồn tại trong DOM nhưng bị ẩn (display:none).
     */
    protected boolean isDisplayed(String locator) {
        return getElement(locator).isDisplayed();
    }

    /**
     * Overload — dùng khi locator là dynamic (có %s).
     */
    protected boolean isDisplayed(String locator, String... restParameter) {
        return getElement(getDynamicLocator(locator, restParameter)).isDisplayed();
    }

    /**
     * Kiểm tra element có đang được chọn không.
     * Dùng cho checkbox và radio button.
     */
    protected boolean isSelected(String locator) {
        return getElement(locator).isSelected();
    }

    /**
     * Overload — dùng khi locator là dynamic (có %s).
     */
    protected boolean isSelected(String locator, String... restParameter) {
        return getElement(getDynamicLocator(locator, restParameter)).isSelected();
    }

    /**
     * Kiểm tra element có đang được enable (tương tác được) không.
     * Dùng để verify button hoặc input field bị disabled.
     */
    protected boolean isEnabled(String locator) {
        return getElement(locator).isEnabled();
    }

    /**
     * Kiểm tra dropdown có phải là multi-select không.
     * Dùng cho <select multiple> tag.
     */
    protected boolean isDropdownMultiple(String locator) {
        waitForElementVisible(locator);
        return new Select(getElement(locator)).isMultiple();
    }


    // =============================================================================
    // 5. ELEMENT — ACTION
    // Tương tác trực tiếp với element: click, nhập text, checkbox, dropdown...
    // =============================================================================

    // --- Click ---

    /**
     * Click vào element
     */
    protected void clickToElement(String locator) {
        waitForElementClickable(locator);
        getElement(locator).click();
    }

    /**
     * Overload — dùng khi locator là dynamic (có %s).
     *
     * Ví dụ:
     *   private String menuItem = "xpath=//li[text()='%s']";
     *   clickToElement(driver, menuItem, "Admin");
     */
    protected void clickToElement(String locator, String... values) {
        waitForElementClickable(getDynamicLocator(locator, values));
        getElement(getDynamicLocator(locator, values)).click();
    }

    // --- Input ---

    /**
     * Xóa nội dung cũ rồi nhập text mới vào input field.
     */
    protected void sendKeysToElement(String locator, String value) {
        waitForElementVisible(locator);
        WebElement element = getElement(locator);
        element.clear();
        element.sendKeys(value);
    }

    // --- Dropdown (HTML <select> tag) ---

    /**
     * Chọn item trong dropdown <select> theo visible text.
     */
    protected void selectItemInDropDown(String locator, String itemText) {
        waitForElementVisible(locator);
        new Select(getElement(locator)).selectByVisibleText(itemText);
    }

    /**
     * Lấy text của item đang được chọn trong dropdown <select>.
     */
    protected String getSelectedItemInDropdown(String locator) {
        waitForElementVisible(locator);
        return new Select(getElement(locator)).getFirstSelectedOption().getText();
    }

    /**
     * Chọn item trong custom dropdown (không phải <select> tag — thường là div/ul/li).
     *
     * Flow:
     *   1. Click vào ô dropdown để mở danh sách
     *   2. Chờ các item xuất hiện
     *   3. Scroll đến item cần chọn rồi click
     *
     * @param parentLocator    Locator của ô dropdown (trigger button)
     * @param childItemLocator Locator của tất cả các item bên trong dropdown
     * @param expectedItem     Text của item cần chọn
     */
    protected void selectItemInCustomDropdown(String parentLocator, String childItemLocator, String expectedItem) {
        waitForElementClickable(parentLocator);
        getElement(parentLocator).click();
        sleepInSeconds(2);

        List<WebElement> allItems = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByLocator(childItemLocator)));

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

    // --- Checkbox & Radio ---

    /**
     * Tick vào checkbox hoặc radio button (chỉ click nếu chưa được chọn).
     */
    protected void checkTheCheckboxOrRadio(String locator) {
        waitForElementClickable(locator);
        if (!getElement(locator).isSelected()) {
            getElement(locator).click();
        }
    }

    /**
     * Overload — dùng khi locator là dynamic (có %s).
     */
    protected void checkTheCheckboxOrRadio(String locator, String... restParameter) {
        waitForElementClickable(locator);
        if (!getElement(getDynamicLocator(locator, restParameter)).isSelected()) {
            getElement(getDynamicLocator(locator, restParameter)).click();
        }
    }

    /**
     * Bỏ tick checkbox (chỉ click nếu đang được chọn).
     */
    protected void uncheckTheCheckbox(String locator) {
        waitForElementClickable(locator);
        if (getElement(locator).isSelected()) {
            getElement(locator).click();
        }
    }

    /**
     * Overload — dùng khi locator là dynamic (có %s).
     */
    protected void uncheckTheCheckbox(String locator, String... restParameter) {
        waitForElementClickable(locator);
        if (getElement(getDynamicLocator(locator, restParameter)).isSelected()) {
            getElement(getDynamicLocator(locator, restParameter)).click();
        }
    }


    // =============================================================================
    // 6. ACTIONS API
    // Các thao tác nâng cao dùng Selenium Actions class.
    // Dùng khi thao tác chuột/bàn phím thông thường không hoạt động.
    // =============================================================================

    /**
     * Khởi tạo Actions object — dùng nội bộ hoặc khi cần chain nhiều action.
     */
    protected Actions getActions() {
        return new Actions(driver);
    }

    /**
     * Click chuột trái vào element bằng Actions.
     * Dùng khi .click() thông thường bị chặn bởi overlay hoặc animation.
     */
    protected void leftClickToElement(String locator) {
        waitForElementClickable(locator);
        getActions().click(getElement(locator)).perform();
    }

    /**
     * Double click vào element.
     */
    protected void doubleClickToElement(String locator) {
        waitForElementClickable(locator);
        getActions().doubleClick(getElement(locator)).perform();
    }

    /**
     * Di chuyển chuột đến element (hover).
     * Dùng để trigger tooltip hoặc hiện sub-menu.
     */
    protected void hoverMouseToElement(String locator) {
        waitForElementVisible(locator);
        getActions().moveToElement(getElement(locator)).perform();
    }

    /**
     * Click chuột phải vào element (context menu).
     */
    protected void rightClickToElement(String locator) {
        waitForElementClickable(locator);
        getActions().contextClick(getElement(locator)).perform();
    }

    /**
     * Kéo thả element từ source đến target.
     */
    protected void dragAndDropToElement(String sourceLocator, String targetLocator) {
        waitForElementVisible(sourceLocator);
        waitForElementVisible(targetLocator);
        getActions().dragAndDrop(getElement(sourceLocator), getElement(targetLocator)).perform();
    }

    /**
     * Scroll trang đến vị trí của element bằng Actions.
     * Selenium 4+ hỗ trợ native — không cần JS.
     */
    protected void scrollToElement(String locator) {
        getActions().scrollToElement(getElement(locator)).perform();
    }

    /**
     * Gửi phím bàn phím đến element (vd: Keys.ENTER, Keys.TAB, Keys.ESCAPE).
     */
    protected void sendKeyboardToElement(String locator, Keys key) {
        waitForElementVisible(locator);
        getActions().sendKeys(getElement(locator), key).perform();
    }

    /**
     * Overload — dùng khi locator là dynamic (có %s).
     */
    protected void sendKeyboardToElement(String locator, Keys key, String... restParameter) {
        waitForElementVisible(locator);
        getActions().sendKeys(getElement(getDynamicLocator(locator, restParameter)), key).perform();
    }


    // =============================================================================
    // 7. JAVASCRIPT EXECUTOR
    // Thực thi JavaScript trực tiếp lên browser.
    // Dùng khi Selenium WebDriver không thể tương tác trực tiếp với element.
    // =============================================================================

    /**
     * Highlight element bằng border đỏ trong 2 giây rồi khôi phục style cũ.
     * Hữu ích khi debug hoặc demo để thấy element nào đang được tương tác.
     */
    protected void highlightElement(String locator) {
        WebElement element = getElement(locator);
        String originalStyle = element.getAttribute("style");
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style', arguments[1])", element, "border: 2px solid red; border-style: dashed;");
        sleepInSeconds(2);
        ((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('style', arguments[1])", element, originalStyle);
    }

    /**
     * Click vào element bằng JavaScript.
     * Dùng khi element bị che bởi overlay hoặc .click() ném ElementClickInterceptedException.
     */
    protected void clickToElementByJS(String locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", getElement(locator));
        sleepInSeconds(3);
    }

    /**
     * Scroll trang để element xuất hiện ở đầu viewport (scrollIntoView = true).
     */
    protected void scrollToElementOnTopByJS(String locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", getElement(locator));
    }

    /**
     * Scroll trang để element xuất hiện ở cuối viewport (scrollIntoView = false).
     */
    protected void scrollToElementOnDownByJS(String locator) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", getElement(locator));
    }

    /**
     * Scroll xuống cuối trang.
     */
    protected void scrollToBottomPageByJS() {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    /**
     * Gán giá trị cho attribute của element trực tiếp trên DOM.
     * Dùng để set readonly field hoặc thay đổi attribute mà Selenium không hỗ trợ.
     */
    protected void setAttributeInDOM(String locator, String attributeName, String attributeValue) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].setAttribute('" + attributeName + "', '" + attributeValue + "');",
                getElement(locator));
    }

    /**
     * Xóa một attribute khỏi element trên DOM.
     * Ví dụ: xóa attribute "disabled" để enable một button bị khóa.
     */
    protected void removeAttributeInDOM(String locator, String attributeRemove) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].removeAttribute('" + attributeRemove + "');",
                getElement(locator));
    }

    /**
     * Nhập text vào input field bằng JavaScript (set attribute value).
     * Dùng cho các field không nhận sendKeys() thông thường.
     */
    protected void sendkeyToElementByJS(String locator, String value) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].setAttribute('value', '" + value + "')",
                getElement(locator));
    }

    /**
     * Lấy giá trị attribute của element thông qua JavaScript.
     * Khác getAttribute() ở chỗ đọc thẳng từ DOM — hữu ích với các attribute ẩn.
     */
    protected String getAttributeInDOMByJS(String locator, String attributeName) {
        return (String) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].getAttribute('" + attributeName + "');",
                getElement(locator));
    }

    /**
     * Lấy thông báo validation HTML5 của input field (vd: "Please fill out this field").
     * Dùng để verify client-side validation mà không cần đọc text trên UI.
     */
    protected String getElementValidationMessage(String locator) {
        return (String) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].validationMessage;",
                getElement(locator));
    }

    /**
     * Kiểm tra ảnh có load thành công không (naturalWidth > 0).
     * Dùng để verify ảnh avatar, product image không bị broken.
     */
    protected boolean isImageLoaded(String locator) {
        return (boolean) ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].complete && typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0",
                getElement(locator));
    }


    // =============================================================================
    // 8. FRAME & WINDOW
    // Xử lý iframe và chuyển đổi context giữa các cửa sổ/tab.
    // =============================================================================

    /**
     * Switch vào trong iframe theo locator.
     * Phải gọi hàm này trước khi tương tác với element bên trong iframe.
     */
    protected void switchToIframe(String locator) {
        driver.switchTo().frame(getElement(locator));
    }

    /**
     * Quay trở lại trang chính (thoát khỏi iframe).
     * Luôn gọi hàm này sau khi xong việc trong iframe.
     */
    protected void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }


    // =============================================================================
    // 9. WAIT
    // Explicit wait và sleep.
    //
    // Ưu tiên dùng waitForElement* thay vì sleepInSeconds.
    // sleepInSeconds chỉ dùng khi không có cách nào khác (vd: animation chưa xong).
    // =============================================================================

    /**
     * Chờ cho element có thể click được (visible + enabled).
     * Dùng trước khi click vào button, link, hoặc bất kỳ element tương tác nào.
     */
    protected void waitForElementClickable(String locator) {
        wait.until(ExpectedConditions.elementToBeClickable(getByLocator(locator)));
    }

    /**
     * Chờ cho element hiển thị trên UI.
     * Dùng trước khi đọc text, attribute hoặc tương tác với element.
     */
    protected void waitForElementVisible(String locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator)));
    }

    /**
     * Chờ cho element biến mất trên UI và DOM
     */
    protected void waitForElementInvisible(String locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locator)));
    }

    /**
     * Chờ cho list elements biến mất trên UI và DOM
     */
    protected void waitForListElementsInvisible(String locator) {
        wait.until(ExpectedConditions.invisibilityOfAllElements(getListElement(locator)));
    }

    /**
     * Sleep cứng theo số giây.
     * Hạn chế dùng — chỉ dùng khi wait condition không áp dụng được
     * (vd: chờ animation, chờ file download, chờ email...).
     *
     * @param timeout số giây cần chờ
     */
    protected void sleepInSeconds(long timeout) {
        try {
            Thread.sleep(timeout * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // =============================================================================
    // 10. BASE PAGE
    // Chứa các hàm dùng chung cho các Page ví dụ như đợi Loading icon biến mất
    // =============================================================================

    /**
     * Đợi cho loading icon biến mất
     */

    protected void waitForLoadingIconInvisible() {
        waitForListElementsInvisible(BasePageUI.LOADING_ICON);
    }
}
