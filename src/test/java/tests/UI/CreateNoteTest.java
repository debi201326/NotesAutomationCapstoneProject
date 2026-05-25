package tests.UI;

import base.BaseTest;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.NotesPage;
import utils.ScreenshotUtil;

public class CreateNoteTest extends BaseTest {

   @Test(description = "TC-UI-02: Create a New Note via UI")
    @Severity(SeverityLevel.CRITICAL)
    public void TC_UI_02_CreateNote() {
        try {
            System.out.println("===== TC-UI-02: Create a New Note via UI and Verify it appears in UI List =====");
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(email, password);
            NotesPage notesPage = new NotesPage(driver, wait);
            notesPage.createNote(noteTitle, noteDescription, noteCategory);
            boolean created = notesPage.isNoteVisible(noteTitle);
            ScreenshotUtil.captureAndAttach(driver, "TC-UI-02 Note Created");
            Assert.assertTrue(created, "Note was not created successfully");
            System.out.println("TC-UI-02 PASSED");

        } catch (Exception e) {
            ScreenshotUtil.captureAndAttach(driver, "TC-UI-02 FAILED");
            Assert.fail("TC-UI-02 Failed: " + e.getMessage());
        }
    }

    @Test(description = "TC-UI-03: Newly Created Note Appears in UI List")
    @Severity(SeverityLevel.CRITICAL)
    public void TC_UI_03_NoteAppearsInList() {
        try {
            System.out.println("===== TC-UI-03: Newly Created Note Appears in UI List =====");
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(email, password);
            NotesPage notesPage = new NotesPage(driver, wait);
            notesPage.createNote(noteTitle, noteDescription, noteCategory);
            boolean visible = notesPage.isNoteVisible(noteTitle);
            ScreenshotUtil.captureAndAttach(driver, "TC-UI-03 Note in List");
            Assert.assertTrue(visible, "Note did not appear instantly in UI list after creation");
            System.out.println("TC-UI-03 PASSED");

        } catch (Exception e) {
            ScreenshotUtil.captureAndAttach(driver, "TC-UI-03 FAILED");
            Assert.fail("TC-UI-03 Failed: " + e.getMessage());

        }
    }

}