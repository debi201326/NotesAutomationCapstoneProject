package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClickUtil {

    public static void click(WebDriver driver, WebDriverWait wait, By locator) {

        int attempts = 0;

        while (attempts < 3) {
            try {
                WebElement element = WaitUtil.waitForPresence(wait, locator);
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                        element);

                WaitUtil.waitForClickable(wait, locator);
                element.click();
                return;

            } catch (ElementClickInterceptedException e) {
                attempts++;
                System.out.println("[WARN] Click intercepted. Retry: " + attempts);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }

            }
        }

        System.out.println("[WARN] Normal click failed. Trying JS click: " + locator);
        jsClick(driver, wait, locator);
    }

    public static void jsClick(WebDriver driver, WebDriverWait wait, By locator) {
        WebElement element = WaitUtil.waitForPresence(wait, locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", element);
        System.out.println("[INFO] JS click performed on: " + locator);
    }
}