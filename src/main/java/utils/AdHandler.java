package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AdHandler {

    public static void dismissAd(WebDriver driver) {
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "var iframes = document.querySelectorAll('iframe');" +
                            "iframes.forEach(function(iframe) { iframe.remove(); });");
            System.out.println("[INFO] Ad iframes removed");
        } catch (Exception e) {
            System.out.println("[WARN] Could not remove iframes: " + e.getMessage());
        }
    }

    public static void dismissVignetteAd(WebDriver driver) {
        try {
            // try clicking the close button if visible
            try {
                WebElement closeBtn = driver.findElement(
                        By.cssSelector("[id*='dismiss-button'], [id*='close'], [aria-label='Close ad']"));
                if (closeBtn.isDisplayed()) {
                    closeBtn.click();
                    System.out.println("[INFO] Vignette close button clicked");
                    return;
                }
            } catch (Exception ignored) {
            }

            // if no close button, remove via javascript
            ((JavascriptExecutor) driver).executeScript(
                    "var vignetteSelectors = [" +
                            "  '[id*=\"vignette\"]'," +
                            "  '[class*=\"vignette\"]'," +
                            "  '[id*=\"interstitial\"]'," +
                            "  'iframe[src*=\"googleads\"]'," +
                            "  'iframe[src*=\"doubleclick\"]'" +
                            "];" +
                            "vignetteSelectors.forEach(function(selector) {" +
                            "  document.querySelectorAll(selector).forEach(function(el) {" +
                            "    el.remove();" +
                            "  });" +
                            "});");
            System.out.println("[INFO] Vignette ad removed");

        } catch (Exception e) {
            System.out.println("[WARN] Could not remove vignette ad: " + e.getMessage());
        }
    }
}