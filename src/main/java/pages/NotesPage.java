package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.AdHandler;
import utils.ClickUtil;
import utils.WaitUtil;

public class NotesPage {
    // Page Object for the Notes Page
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators for notes page elements
    private By addNoteBtn = By.xpath("//button[contains(text(),'Add Note')]");
    private By titleField = By.id("title");
    private By descField = By.id("description");
    private By categoryField = By.id("category");
    private By createButton = By.xpath("//button[@data-testid='note-submit']");

    // Constructor to initialize WebDriver and WebDriverWait
    public NotesPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    // Creates a new note with the given title, description, and category. Handles ads before interacting with the page elements.
    public void createNote(String title, String description, String category) {
        WaitUtil.waitForVisible(wait, addNoteBtn);
        AdHandler.dismissAd(driver);
        AdHandler.dismissVignetteAd(driver);
        ClickUtil.jsClick(driver, wait, addNoteBtn);
        driver.findElement(titleField).sendKeys(title);
        driver.findElement(descField).sendKeys(description);
        if (category != null && !category.isEmpty()) {
            new Select(driver.findElement(categoryField)).selectByVisibleText(category);
        }
        AdHandler.dismissAd(driver);
        AdHandler.dismissVignetteAd(driver);
        ClickUtil.jsClick(driver, wait, createButton);
        System.out.println("Note created: " + title + " [" + category + "]");
    }

    // Checks if a note with the given title is visible on the page
    public boolean isNoteVisible(String title) {
        try {
            String text = WaitUtil.waitForVisible(wait, By.xpath("//*[contains(text(),'" + title + "')]")).getText();
            return text.contains(title);
        } catch (Exception e) {
            return false;
        }
    }

    // Refreshes the page and checks if a note with the given title is no longer present in the page source
    public boolean isNoteGoneAfterRefresh(WebDriver driver, String title) {
        driver.navigate().refresh();
        return !driver.getPageSource().contains(title);
    }
}