package java_OOP;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;


public class BrowserConfig_AddExtension {

    public static void main(String[] args) throws InterruptedException {

        String extensionPath =
                System.getProperty("user.dir")
                        + File.separator
                        + "extension"
                        + File.separator
                        + "wpsPDF_unpacked";

        File extensionFolder = new File(extensionPath);
        File manifestFile = new File(extensionFolder, "manifest.json");

        System.out.println("Extension path: " + extensionFolder.getAbsolutePath());
        System.out.println("Extension exists: " + extensionFolder.exists());
        System.out.println("Manifest exists: " + manifestFile.exists());

        if (!manifestFile.exists()) {
            throw new RuntimeException(
                    "manifest.json not found: " + manifestFile.getAbsolutePath()
            );
        }

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--load-extension=" + extensionFolder.getAbsolutePath());

        options.addArguments("--enable-logging");
        options.addArguments("--v=1");

        WebDriver driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        driver.get("chrome://extensions/");

        Thread.sleep(15000);

        driver.quit();

    }
}
