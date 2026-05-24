package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtil {

    public static WebElement waitForVisible(WebDriverWait wait, By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(WebDriverWait wait, By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForPresence(WebDriverWait wait, By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static boolean waitForText(WebDriverWait wait, By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static boolean waitForInvisible(WebDriverWait wait, By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static boolean waitForUrlContains(WebDriverWait wait, String urlPart) {
        return wait.until(ExpectedConditions.urlContains(urlPart));
    }

    public static boolean waitForPageSourceContains(WebDriverWait wait, WebDriver driver, String text) {
        return wait.until(d -> d.getPageSource().contains(text));
    }
}