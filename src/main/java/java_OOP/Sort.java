package java_OOP;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.SQLOutput;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sort {

    public static final String URL = "https://www.saucedemo.com/";
    public static final String USERNAME_TEXTBOX = "input#user-name";
    public static final String PASSWORD_TEXTBOX = "input#password";
    public static final String LOGIN_BUTTON = "input#login-button";
    public static final String SELECT_DROPDOWN = "select.product_sort_container";
    public static WebDriver driver = new ChromeDriver();
    public static WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

    public static void main(String[] args) throws InterruptedException {

        driver.manage().window().maximize();

        driver.get(URL);
        // Login
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(USERNAME_TEXTBOX)));
        driver.findElement(By.cssSelector(USERNAME_TEXTBOX)).sendKeys("standard_user");
        driver.findElement(By.cssSelector(PASSWORD_TEXTBOX)).sendKeys("secret_sauce");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(USERNAME_TEXTBOX)));
        driver.findElement(By.cssSelector(LOGIN_BUTTON)).click();

        Thread.sleep(5000);

        // Click to Dropdown
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(SELECT_DROPDOWN)));
        new Select(driver.findElement(By.cssSelector(SELECT_DROPDOWN))).selectByVisibleText("Price (low to high)");
        Thread.sleep(5000);

        if (isProductPriceAcsending()) {
            System.out.println("Sorted successfully");
        } else
        {
            System.out.println("Sorted failed");
        }

        driver.quit();

    }

    public static boolean isProductNameDescending() {
        // Sort
        // Find all web elements to search
        List<WebElement> productNames = driver.findElements(By.cssSelector("div.inventory_item_name"));

        // Delare a arraylist - call A
        ArrayList<String> productLists = new ArrayList<String>();

        // For to get all the text from ProductNames
        for (WebElement productName : productNames) {
            productLists.add(productName.getText());
        }

        // Clone A to create an ArrayList B
        ArrayList<String> sortedProductList = new ArrayList<String>(productLists);

        Collections.sort(sortedProductList);
        Collections.reverse(sortedProductList);

        boolean isSortedSuccessfully =sortedProductList.equals(productLists);

        return isSortedSuccessfully;

    }

    public static boolean isProductNameAscending() {
        // Sort
        // Find all Product name web elements
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.inventory_item_name")));
        List<WebElement> productNameElements = driver.findElements(By.cssSelector("div.inventory_item_name"));

        // Create a ArrayList to storge all the Product Name text
        ArrayList<String> productNamesList = new ArrayList<String>();
        for (WebElement productNameElement : productNameElements) {
            System.out.println(productNameElement);
            productNamesList.add(productNameElement.getText());
        }

        // Copy to new ArrayList to compare
        ArrayList<String> sortedProductNamesList = new ArrayList<String>(productNamesList);

        Collections.sort(sortedProductNamesList);

        boolean isSortedSuccessfully = sortedProductNamesList.equals(productNamesList);
        return isSortedSuccessfully;
    }

    public static boolean isProductPriceAcsending() {

        // List out all Price web elements
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.inventory_item_price")));
        List<WebElement> productPriceListWebElements = driver.findElements(By.cssSelector("div.inventory_item_price"));

        // Create a ArrayList to storge all price value
        ArrayList<Double> producePriceValues = new ArrayList<Double>();
        for (WebElement productPriceListWebElement : productPriceListWebElements) {
            System.out.println(productPriceListWebElement.getText());
            String priceText = productPriceListWebElement.getText().replace("$", "");
            producePriceValues.add(Double.parseDouble(priceText));
        }

        // Create a copy ArrayList to sort
        ArrayList<Double> sortedProductPriceValues = new ArrayList<>(producePriceValues);

        Collections.sort(sortedProductPriceValues);

        return sortedProductPriceValues.equals(producePriceValues);
    }

    
}
