package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.AdHandler;
import utils.ClickUtil;
import utils.WaitUtil;

public class LoginPage {
    // Page Object for the Login Page
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators for login page elements
    private By loginLink   = By.linkText("Login");
    private By emailField  = By.id("email");
    private By passField   = By.id("password");
    private By loginButton = By.xpath("//button[text()='Login']");
    private By dashboard   = By.cssSelector("[data-testid='logout']");

    // Constructor to initialize WebDriver and WebDriverWait
    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

   // Logs in using the provided email and password, with ad handling and waiting for dashboard visibility to confirm successful login
    public void login(String email, String password) {
        ClickUtil.click(driver, wait, loginLink);
        AdHandler.dismissAd(driver);
        AdHandler.dismissVignetteAd(driver);
        WaitUtil.waitForVisible(wait, emailField).sendKeys(email);
        driver.findElement(passField).sendKeys(password);
        AdHandler.dismissAd(driver);
        AdHandler.dismissVignetteAd(driver);
        ClickUtil.jsClick(driver, wait, loginButton);
        WaitUtil.waitForVisible(wait, dashboard);
        System.out.println("Login successful");
    }

    // Similar to login method but does not wait for dashboard visibility, useful for performance testing
    public void loginWithoutWait(String email, String password) {
        ClickUtil.click(driver, wait, loginLink);
        AdHandler.dismissAd(driver);
        AdHandler.dismissVignetteAd(driver);
        WaitUtil.waitForVisible(wait, emailField).sendKeys(email);
        driver.findElement(passField).sendKeys(password);
        AdHandler.dismissAd(driver);
        AdHandler.dismissVignetteAd(driver);
        ClickUtil.jsClick(driver, wait, loginButton);
        System.out.println("Login attempt submitted");
    }
}