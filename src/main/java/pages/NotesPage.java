package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.AdHandler;
import utils.ClickUtil;
import utils.WaitUtil;

public class NotesPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By addNoteBtn = By.xpath("//button[contains(text(),'Add Note')]");
    private By titleField = By.id("title");
    private By descField = By.id("description");
    private By categoryField = By.id("category");
    private By createButton = By.xpath("//button[@data-testid='note-submit']");

    public NotesPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

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

    public boolean isNoteVisible(String title) {
        try {
            String text = WaitUtil.waitForVisible(wait, By.xpath("//*[contains(text(),'" + title + "')]")).getText();
            return text.contains(title);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNoteGoneAfterRefresh(WebDriver driver, String title) {
        driver.navigate().refresh();
        return !driver.getPageSource().contains(title);
    }
}