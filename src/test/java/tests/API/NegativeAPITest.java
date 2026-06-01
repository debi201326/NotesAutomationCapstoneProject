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

public class NegativeAPITest extends BaseTest {

        @BeforeClass
        public void apiSetup() {
                APIAuthentication.generateToken();
                RestAssured.baseURI = ConfigReader.get("api.base.url");
        }

        @Test(description = "TC-NEG-03: Create Note via API without Token")
        @Severity(SeverityLevel.NORMAL)
        public void TC_NEG_03_CreateNoteWithoutToken() {
                System.out.println("TC-NEG-03: Create Note via API without Token");
                Response response = RestAssured
                                .given()
                                .contentType("application/json")
                                .body("{"
                                                + "\"title\":\"" + noteTitle + "\","
                                                + "\"description\":\"" + noteDescription + "\","
                                                + "\"category\":\"" + noteCategory + "\""
                                                + "}")
                                .post("/notes");
                System.out.println("Status: " + response.statusCode());
                Assert.assertEquals(response.statusCode(), 401, "Expected 401 but got: " + response.statusCode());
                System.out.println("TC-NEG-03 PASSED");
        }

        @Test(description = "TC-NEG-04: GET /notes with Invalid Token")
        @Severity(SeverityLevel.NORMAL)
        public void TC_NEG_04_InvalidToken() {
                System.out.println("TC-NEG-04: GET /notes with Invalid Token");
                Response response = RestAssured
                                .given()
                                .header("x-auth-token", "invalidtoken123abc")
                                .get("/notes");
                System.out.println("Status: " + response.statusCode());
                Assert.assertEquals(response.statusCode(), 401, "Expected 401 but got: " + response.statusCode());
                System.out.println("TC-NEG-04 PASSED");
        }

        @Test(description = "TC-NEG-05: DELETE Note with Wrong Note ID")
        @Severity(SeverityLevel.NORMAL)
        public void TC_NEG_05_DeleteWrongNoteId() {
                System.out.println("TC-NEG-05: DELETE Note with Wrong Note ID");
                Response response = APIAuthentication.getBaseSpec()
                                .delete("/notes/wrongid000abc");
                int status = response.statusCode();
                System.out.println("Status: " + status);
                Assert.assertTrue(status == 400 || status == 404, "Expected 400 or 404 but got: " + status);
                System.out.println("TC-NEG-05 PASSED");
        }
}