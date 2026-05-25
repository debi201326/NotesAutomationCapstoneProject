package drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverManager {

    // ThreadLocal so parallel tests dont share same driver
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Provides access to the WebDriver instance for the current thread
    public static WebDriver getDriver() {
        return driver.get();
    }
    
    // Initializes the WebDriver instance with ChromeOptions and sets it in ThreadLocal
    public static void initDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver.set(new ChromeDriver(options));
    }

    // Closes the WebDriver instance for the current thread
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}