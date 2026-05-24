package tests.API;

import api.APIAuthentication;
import base.BaseTest;
import config.ConfigReader;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.SchemaValidator;

public class DeleteNoteAPITest extends BaseTest {

    @BeforeClass
    public void apiSetup() {
        APIAuthentication.generateToken();
        RestAssured.baseURI = ConfigReader.get("api.base.url");
    }

    @Test(description = "TC-API-02: Delete a Note via API")
    @Severity(SeverityLevel.CRITICAL)
    public void TC_API_02_DeleteNote() {

        System.out.println("===== TC-API-02: Delete a Note via API =====");

        System.out.println("Creating a note to delete...");
        Response createResponse = APIAuthentication.getBaseSpec()
                .body("{\"title\":\"API Delete Test Note\","
                        + "\"description\":\"created to be deleted\","
                        + "\"category\":\"Work\"}")
                .post("/notes");

        System.out.println("Create response status: " + createResponse.statusCode());
        SchemaValidator.validate(createResponse, "create_note_schema.json");

        String noteIdToDelete = createResponse.jsonPath().getString("data.id");
        System.out.println("Note created with ID: " + noteIdToDelete);

        Assert.assertNotNull(noteIdToDelete, "Note ID is null - note was not created");

        System.out.println("Sending DELETE request for note ID: " + noteIdToDelete);
        Response deleteResponse = APIAuthentication.getBaseSpec()
                .delete("/notes/" + noteIdToDelete);

        System.out.println("Delete response status: " + deleteResponse.statusCode());
        System.out.println("Delete response body: " + deleteResponse.getBody().asString());

        Assert.assertEquals(deleteResponse.statusCode(), 200,
                "Expected 200 but got: " + deleteResponse.statusCode());

        System.out.println("TC-API-02 PASSED");
    }
}