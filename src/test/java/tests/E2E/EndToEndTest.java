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
import utils.WaitUtil;
import pages.LoginPage;
import pages.NotesPage;
import utils.ScreenshotUtil;

public class EndToEndTest extends BaseTest {

        public static String capturedNoteId;

        @Test(description = "TC-E2E-01: UI Created Note Visible in API")
        @Severity(SeverityLevel.BLOCKER)
        public void TC_E2E_01_UICreatedNoteVisibleInAPI() {

                System.out.println("===== TC-E2E-01: UI Created Note Visible in API =====");

                try {

                        LoginPage loginPage = new LoginPage(driver, wait);
                        loginPage.login(email, password);

                        NotesPage notesPage = new NotesPage(driver, wait);
                        notesPage.createNote(noteTitle, noteDescription);

                        boolean visible = notesPage.isNoteVisible(noteTitle);

                        Assert.assertTrue(
                                        visible,
                                        "Note not visible in UI");

                        ScreenshotUtil.captureAndAttach(
                                        driver,
                                        "TC-E2E-01 Note in UI");

                        APIAuthentication.generateToken();

                        RestAssured.baseURI = ConfigReader.get("api.base.url");

                        Response getResponse = RestAssured
                                        .given()
                                        .header(
                                                        "x-auth-token",
                                                        APIAuthentication.token)
                                        .get("/notes");

                        Assert.assertEquals(
                                        getResponse.statusCode(),
                                        200);

                        String body = getResponse.getBody().asString();

                        Assert.assertTrue(
                                        body.contains(noteTitle),
                                        "Note created in UI not found in API response");

                        capturedNoteId = getResponse.jsonPath()
                                        .getString("data[0].id");

                        System.out.println(
                                        "Note found in API - ID: "
                                                        + capturedNoteId);

                        System.out.println("TC-E2E-01 PASSED");

                } catch (Exception e) {

                        ScreenshotUtil.captureAndAttach(
                                        driver,
                                        "TC-E2E-01 FAILED");

                        Assert.fail(
                                        "TC-E2E-01 Failed: "
                                                        + e.getMessage());
                }
        }

        @Test(description = "TC-E2E-02: API Deleted Note Disappears from UI", dependsOnMethods = "TC_E2E_01_UICreatedNoteVisibleInAPI")
        @Severity(SeverityLevel.BLOCKER)
        public void TC_E2E_02_APIDeletedNoteGoneFromUI() {

                System.out.println("===== TC-E2E-02: API Deleted Note Disappears from UI =====");

                try {

                        APIAuthentication.generateToken();

                        RestAssured.baseURI = ConfigReader.get("api.base.url");

                        Assert.assertNotNull(
                                        capturedNoteId,
                                        "Note ID not available - TC-E2E-01 may have failed");

                        Response deleteResponse = RestAssured
                                        .given()
                                        .header(
                                                        "x-auth-token",
                                                        APIAuthentication.token)
                                        .delete("/notes/" + capturedNoteId);

                        System.out.println(
                                        "Delete Status: "
                                                        + deleteResponse.statusCode());

                        Assert.assertEquals(
                                        deleteResponse.statusCode(),
                                        200,
                                        "Delete API did not return 200");

                        LoginPage loginPage = new LoginPage(driver, wait);
                        loginPage.login(email, password);

                        driver.navigate().refresh();

                        boolean isGone = !driver.getPageSource()
                                        .contains(noteTitle);

                        ScreenshotUtil.captureAndAttach(
                                        driver,
                                        "TC-E2E-02 Note Deleted from UI");

                        Assert.assertTrue(
                                        isGone,
                                        "Deleted note still appears in UI after API deletion");

                        System.out.println("TC-E2E-02 PASSED");

                } catch (Exception e) {

                        ScreenshotUtil.captureAndAttach(
                                        driver,
                                        "TC-E2E-02 FAILED");

                        Assert.fail(
                                        "TC-E2E-02 Failed: "
                                                        + e.getMessage());
                }
        }

        @Test(description = "TC-E2E-03: UI and API Data Consistency Check")
        @Severity(SeverityLevel.CRITICAL)
        public void TC_E2E_03_UIAPIDataConsistency() {

                System.out.println("===== TC-E2E-03: UI and API Data Consistency Check =====");

                try {

                        LoginPage loginPage = new LoginPage(driver, wait);
                        loginPage.login(email, password);

                        NotesPage notesPage = new NotesPage(driver, wait);
                        notesPage.createNote(noteTitle, noteDescription);

                        String uiTitle = WaitUtil.waitForVisible(
                                        wait,
                                        By.xpath(
                                                        "//*[contains(text(),'"
                                                                                        + noteTitle
                                                                                        + "')]"))
                                        .getText().trim();

                        System.out.println("UI Title: " + uiTitle);

                        ScreenshotUtil.captureAndAttach(
                                        driver,
                                        "TC-E2E-03 UI Note Data");

                        APIAuthentication.generateToken();

                        RestAssured.baseURI = ConfigReader.get("api.base.url");

                        Response getResponse = RestAssured
                                        .given()
                                        .header(
                                                        "x-auth-token",
                                                        APIAuthentication.token)
                                        .get("/notes");

                        Assert.assertEquals(
                                        getResponse.statusCode(),
                                        200);

                        String apiTitle = getResponse.jsonPath()
                                        .getString("data[0].title");

                        String apiDescription = getResponse.jsonPath()
                                        .getString("data[0].description");

                        System.out.println(
                                        "API Title: " + apiTitle);

                        System.out.println(
                                        "API Description: "
                                                        + apiDescription);

                        Assert.assertEquals(
                                        apiTitle,
                                        noteTitle,
                                        "Title mismatch - UI: "
                                                        + uiTitle
                                                        + " API: "
                                                        + apiTitle);

                        Assert.assertEquals(
                                        apiDescription,
                                        noteDescription,
                                        "Description mismatch - UI: "
                                                        + noteDescription
                                                        + " API: "
                                                        + apiDescription);

                        System.out.println(
                                        "All fields matched between UI and API");

                        System.out.println("TC-E2E-03 PASSED");

                } catch (Exception e) {

                        ScreenshotUtil.captureAndAttach(
                                        driver,
                                        "TC-E2E-03 FAILED");

                        Assert.fail(
                                        "TC-E2E-03 Failed: "
                                                        + e.getMessage());
                }
        }
}