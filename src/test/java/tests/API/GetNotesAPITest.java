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

public class GetNotesAPITest extends BaseTest {

    @BeforeClass
    public void apiSetup() {
        APIAuthentication.generateToken();
        RestAssured.baseURI = ConfigReader.get("api.base.url");
    }

    @Test(description = "TC-API-01: GET /notes Returns Notes List")
    @Severity(SeverityLevel.CRITICAL)
    public void TC_API_01_GetNotes() {

        System.out.println("TC-API-01: GET /notes Returns Notes List");
        Response response = APIAuthentication.getWithRetry("/notes", 3);
        System.out.println("Status Code: " + response.statusCode());
        Assert.assertEquals(response.statusCode(), 200, "GET /notes did not return 200");
        String body = response.getBody().asString();
        Assert.assertTrue(body.contains("data"), "Response does not contain data field");
        SchemaValidator.validate(response, "get_notes_schema.json");
        System.out.println("TC-API-01 PASSED");
    }
}