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

public class ResponseTimeAPITest extends BaseTest {

        @BeforeClass
        public void apiSetup() {
                APIAuthentication.generateToken();
                RestAssured.baseURI = ConfigReader.get("api.base.url");
        }

        @Test(description = "TC-API-03: API Response Time is Under 2 Seconds")
        @Severity(SeverityLevel.NORMAL)
        public void TC_API_03_ResponseTimeUnder2Seconds() {
                System.out.println("TC-API-03: API Response Time Under 2 Seconds");
                Response response = APIAuthentication.getWithRetry("/notes", 3);
                Assert.assertNotNull(response, "API response is null. Request failed after retries.");
                int statusCode = response.getStatusCode();
                Assert.assertEquals(statusCode, 200, "Expected status code 200 but received " + statusCode);
                long responseTime = response.getTime();
                System.out.println("API Response Time: " + responseTime + "ms");
                Assert.assertTrue(responseTime < 2000, "Response time exceeded the 2000ms threshold.");
                System.out.println("TC-API-03 PASSED");
        }
}