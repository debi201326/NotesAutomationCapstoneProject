package tests.E2E;

import api.APIAuthentication;
import base.BaseTest;
import config.ConfigReader;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.NotesPage;
import utils.ScreenshotUtil;
import utils.WaitUtil;

public class EndToEndTest extends BaseTest {

    @Test(description = "TC-E2E-01: Complete Notes App Flow - Login, Create, Verify, Delete")
    @Severity(SeverityLevel.BLOCKER)
    public void TC_E2E_01_CompleteFlow() {
        System.out.println("===== TC-E2E-01: Complete End to End Flow =====");

        try {

            // FR-01 UI LOGIN
            System.out.println("[STEP 1] FR-01: Login via UI");
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(email, password);
            boolean loggedIn = WaitUtil.waitForVisible(wait,
                By.cssSelector("[data-testid='logout']")).isDisplayed();
            Assert.assertTrue(loggedIn, "FR-01 Failed: Login did not work");
            ScreenshotUtil.captureAndAttach(driver, "Step1 - Login Success");
            System.out.println("[STEP 1] FR-01 PASSED");

            // FR-02 CREATE NOTE VIA UI
            System.out.println("[STEP 2] FR-02: Create note via UI");
            NotesPage notesPage = new NotesPage(driver, wait);
            notesPage.createNote(noteTitle, noteDescription, noteCategory);
            ScreenshotUtil.captureAndAttach(driver, "Step2 - Note Created");
            System.out.println("[STEP 2] FR-02 PASSED");

            // FR-03 NOTE APPEARS IN UI LIST
            System.out.println("[STEP 3] FR-03: Verify note appears in UI list");
            boolean visibleInUI = notesPage.isNoteVisible(noteTitle);
            Assert.assertTrue(visibleInUI, "FR-03 Failed: Note did not appear in UI list");
            ScreenshotUtil.captureAndAttach(driver, "Step3 - Note Visible in UI");
            System.out.println("[STEP 3] FR-03 PASSED");

            // FR-04 API GET /notes RETURNS LIST
            System.out.println("[STEP 4] FR-04: GET /notes API returns list");
            APIAuthentication.generateToken();
            RestAssured.baseURI = ConfigReader.get("api.base.url");

            Response getResponse = RestAssured
                .given()
                .header("x-auth-token", APIAuthentication.token)
                .get("/notes");

            Assert.assertEquals(getResponse.statusCode(), 200,
                "FR-04 Failed: GET /notes did not return 200");
            System.out.println("[STEP 4] FR-04 PASSED");

            // FR-05 UI CREATED NOTE VISIBLE IN API
            System.out.println("[STEP 5] FR-05: UI created note visible in API");
            String body = getResponse.getBody().asString();
            Assert.assertTrue(body.contains(noteTitle),
                "FR-05 Failed: Note created in UI not found in API response");

            String apiTitle       = getResponse.jsonPath().getString("data[0].title");
            String apiDescription = getResponse.jsonPath().getString("data[0].description");
            String noteId         = getResponse.jsonPath().getString("data[0].id");

            Assert.assertEquals(apiTitle, noteTitle,
                "FR-05 Failed: Title mismatch - UI: " + noteTitle + " API: " + apiTitle);
            Assert.assertEquals(apiDescription, noteDescription,
                "FR-05 Failed: Description mismatch");

            System.out.println("Note ID: " + noteId);
            System.out.println("UI Title: " + noteTitle + " API Title: " + apiTitle);
            System.out.println("[STEP 5] FR-05 PASSED");

            // FR-06 DELETE NOTE VIA API
            System.out.println("[STEP 6] FR-06: Delete note via API");
            Response deleteResponse = RestAssured
                .given()
                .header("x-auth-token", APIAuthentication.token)
                .delete("/notes/" + noteId);

            Assert.assertEquals(deleteResponse.statusCode(), 200,
                "FR-06 Failed: Delete API did not return 200");
            System.out.println("[STEP 6] FR-06 PASSED");

            // FR-07 DELETED NOTE DISAPPEARS FROM UI
            System.out.println("[STEP 7] FR-07: Deleted note disappears from UI");
            driver.navigate().refresh();
            boolean isGone = !driver.getPageSource().contains(noteTitle);
            ScreenshotUtil.captureAndAttach(driver, "Step7 - Note Gone from UI");
            Assert.assertTrue(isGone,
                "FR-07 Failed: Deleted note still appears in UI");
            System.out.println("[STEP 7] FR-07 PASSED");

            System.out.println("===== TC-E2E-01: ALL STEPS PASSED =====");

        } catch (Exception e) {
            ScreenshotUtil.captureAndAttach(driver, "TC-E2E-01 FAILED");
            Assert.fail("TC-E2E-01 Failed: " + e.getMessage());
        }
    }
}