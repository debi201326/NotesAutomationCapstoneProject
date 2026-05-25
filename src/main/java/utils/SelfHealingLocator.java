package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SelfHealingLocator {

    // tries all provided locators one by one until one works
    public static WebElement findElement(WebDriver driver, WebDriverWait wait, By... locators) {

        for (By locator : locators) {
            try {
                WebElement element = WaitUtil.waitForPresence(wait, locator);
                if (element != null && element.isDisplayed()) {
                    System.out.println("[SELF-HEAL] Element found using: " + locator);
                    return element;
                }
            } catch (Exception e) {
                System.out.println("[SELF-HEAL] Locator failed, trying next: " + locator);
            }
        }

        throw new RuntimeException("[SELF-HEAL] All locators failed. Element not found.");
    }

    // self healing click - tries all locators then falls back to js click
    public static void click(WebDriver driver, WebDriverWait wait, By... locators) {
        WebElement element = findElement(driver, wait, locators);
        try {
            element.click();
            System.out.println("[SELF-HEAL] Normal click successful");
        } catch (Exception e) {
            System.out.println("[SELF-HEAL] Normal click failed, using JS click");
            ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", element);
        }
    }

    // self healing sendKeys - finds element and types
    public static void sendKeys(WebDriver driver, WebDriverWait wait, String text, By... locators) {
        WebElement element = findElement(driver, wait, locators);
        try {
            element.clear();
            element.click();
            element.sendKeys(text);
            System.out.println("[SELF-HEAL] sendKeys successful: " + text);
        } catch (Exception e) {
            System.out.println("[SELF-HEAL] sendKeys failed: " + e.getMessage());
            throw new RuntimeException("[SELF-HEAL] Could not type into element");
        }
    }

    // self healing getText - finds element and returns text
    public static String getText(WebDriver driver, WebDriverWait wait, By... locators) {
        WebElement element = findElement(driver, wait, locators);
        try {
            String text = element.getText();
            System.out.println("[SELF-HEAL] getText successful: " + text);
            return text;
        } catch (Exception e) {
            System.out.println("[SELF-HEAL] getText failed: " + e.getMessage());
            throw new RuntimeException("[SELF-HEAL] Could not get text from element");
        }
    }

    // self healing getAttribute
    public static String getAttribute(WebDriver driver, WebDriverWait wait,
            String attribute, By... locators) {
        WebElement element = findElement(driver, wait, locators);
        try {
            String value = element.getAttribute(attribute);
            System.out.println("[SELF-HEAL] getAttribute successful: " + attribute + "=" + value);
            return value;
        } catch (Exception e) {
            System.out.println("[SELF-HEAL] getAttribute failed: " + e.getMessage());
            throw new RuntimeException("[SELF-HEAL] Could not get attribute from element");
        }
    }

    // self healing isDisplayed
    public static boolean isDisplayed(WebDriver driver, WebDriverWait wait, By... locators) {
        try {
            WebElement element = findElement(driver, wait, locators);
            boolean displayed = element.isDisplayed();
            System.out.println("[SELF-HEAL] isDisplayed: " + displayed);
            return displayed;
        } catch (Exception e) {
            System.out.println("[SELF-HEAL] isDisplayed failed - element not found");
            return false;
        }
    }

    // self healing isEnabled
    public static boolean isEnabled(WebDriver driver, WebDriverWait wait, By... locators) {
        try {
            WebElement element = findElement(driver, wait, locators);
            boolean enabled = element.isEnabled();
            System.out.println("[SELF-HEAL] isEnabled: " + enabled);
            return enabled;
        } catch (Exception e) {
            System.out.println("[SELF-HEAL] isEnabled failed - element not found");
            return false;
        }
    }

    // self healing isSelected - useful for checkboxes
    public static boolean isSelected(WebDriver driver, WebDriverWait wait, By... locators) {
        try {
            WebElement element = findElement(driver, wait, locators);
            boolean selected = element.isSelected();
            System.out.println("[SELF-HEAL] isSelected: " + selected);
            return selected;
        } catch (Exception e) {
            System.out.println("[SELF-HEAL] isSelected failed - element not found");
            return false;
        }
    }

    // self healing submit
    public static void submit(WebDriver driver, WebDriverWait wait, By... locators) {
        WebElement element = findElement(driver, wait, locators);
        try {
            element.submit();
            System.out.println("[SELF-HEAL] submit successful");
        } catch (Exception e) {
            System.out.println("[SELF-HEAL] submit failed, trying click instead");
            element.click();
        }
    }

    // self healing clear
    public static void clear(WebDriver driver, WebDriverWait wait, By... locators) {
        WebElement element = findElement(driver, wait, locators);
        try {
            element.clear();
            System.out.println("[SELF-HEAL] clear successful");
        } catch (Exception e) {
            System.out.println("[SELF-HEAL] clear failed: " + e.getMessage());
            throw new RuntimeException("[SELF-HEAL] Could not clear element");
        }
    }
}