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

                System.out.println("TC-API-02: Delete a Note via API");
                System.out.println("Creating a note to delete...");

                Response createResponse = APIAuthentication.getBaseSpec()
                                .body("{\"title\":\"API Delete Test Note\","
                                                + "\"description\":\"created to be deleted\","
                                                + "\"category\":\"Work\"}")
                                .post("/notes");

                System.out.println("Create response status: " + createResponse.statusCode());
                SchemaValidator.validate(createResponse, ConfigReader.get("create_notes_schema_path"));
                String noteIdToDelete = createResponse.jsonPath().getString("data.id");
                System.out.println("Note created with ID: " + noteIdToDelete);
                Assert.assertNotNull(noteIdToDelete, "Note ID is null - note was not created");
                System.out.println("Sending DELETE request for note ID: " + noteIdToDelete);

                Response deleteResponse = APIAuthentication.getBaseSpec()
                                .delete("/notes/" + noteIdToDelete);

                int statusCode = deleteResponse.getStatusCode();
                System.out.println("Delete response status: " + statusCode);
                SchemaValidator.validate(deleteResponse, ConfigReader.get("delete_notes_schema_path"));
                Assert.assertEquals(statusCode, 200, "Expected 200 but got: " + statusCode);
                System.out.println("TC-API-02 PASSED");
        }
}