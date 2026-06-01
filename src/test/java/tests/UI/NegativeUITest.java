package tests.UI;

import base.BaseTest;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import java.util.Map;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.NotesPage;
import utils.ScreenshotUtil;
import utils.WaitUtil;
import utils.CSVReader;

public class NegativeUITest extends BaseTest {
        @Test(description = "TC-NEG-01: Create Note with Empty Title")
        @Severity(SeverityLevel.NORMAL)
        public void TC_NEG_01_CreateNoteEmptyTitle() {
                try {
                        System.out.println("TC-NEG-01: Create Note with Empty Title");
                        Map<String, String> noteData = CSVReader.getNotesRowByType("empty_title");
                        String emptyTitle = noteData.get("noteTitle");
                        String description = noteData.get("noteDescription");
                        LoginPage loginPage = new LoginPage(driver, wait);
                        loginPage.login(email, password);
                        NotesPage notesPage = new NotesPage(driver, wait);
                        notesPage.createNote(emptyTitle, description, noteCategory);
                        Thread.sleep(1500);
                        boolean validationShown = driver.getPageSource().contains("Title is required");
                        ScreenshotUtil.captureAndAttach(driver, "TC-NEG-01 Empty Title Validation");
                        Assert.assertTrue(validationShown, "Validation not shown when note title is empty");
                        System.out.println("TC-NEG-01 PASSED");

                } catch (Exception e) {
                        ScreenshotUtil.captureAndAttach(driver, "TC-NEG-01 FAILED");
                        Assert.fail("TC-NEG-01 Failed: " + e.getMessage());
                }
        }

        @Test(description = "TC-NEG-02: Create Note with Empty Description")
        @Severity(SeverityLevel.NORMAL)
        public void TC_NEG_02_CreateNoteEmptyDescription() {
                try {
                        System.out.println("TC-NEG-02: Create Note with Empty Description");
                        Map<String, String> noteData = CSVReader.getNotesRowByType("empty_description");
                        String title = noteData.get("noteTitle");
                        String emptyDescription = noteData.get("noteDescription");
                        LoginPage loginPage = new LoginPage(driver, wait);
                        loginPage.login(email, password);
                        NotesPage notesPage = new NotesPage(driver, wait);
                        notesPage.createNote(title, emptyDescription, noteCategory);
                        Thread.sleep(1500);
                        boolean validationShown = driver.getPageSource().contains("Description is required");
                        ScreenshotUtil.captureAndAttach(driver, "TC-NEG-02 Empty Description Validation");
                        Assert.assertTrue(validationShown, "Validation not shown when note description is empty");
                        System.out.println("TC-NEG-02 PASSED");

                } catch (Exception e) {
                        ScreenshotUtil.captureAndAttach(driver, "TC-NEG-02 FAILED");
                        Assert.fail("TC-NEG-02 Failed: " + e.getMessage());
                }
        }

}