package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.AdHandler;
import utils.ClickUtil;
import utils.WaitUtil;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By loginLink   = By.linkText("Login");
    private By emailField  = By.id("email");
    private By passField   = By.id("password");
    private By loginButton = By.xpath("//button[text()='Login']");
    private By dashboard   = By.cssSelector("[data-testid='logout']");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

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