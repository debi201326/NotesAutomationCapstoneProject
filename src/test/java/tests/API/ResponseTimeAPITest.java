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
import utils.PerformanceUtil;

public class ResponseTimeAPITest extends BaseTest {

        @BeforeClass
        public void apiSetup() {
                APIAuthentication.generateToken();
                RestAssured.baseURI = ConfigReader.get("api.base.url");
        }

        @Test(description = "TC-API-03: API Response Time is Under 2 Seconds")
        @Severity(SeverityLevel.NORMAL)
        public void TC_API_03_ResponseTimeUnder2Seconds() {
                System.out.println("===== TC-API-03: API Response Time Under 2 Seconds =====");
                long start = System.currentTimeMillis();
                Response response = APIAuthentication.getWithRetry("/notes", 3);
                long responseTime = System.currentTimeMillis() - start;
                PerformanceUtil.checkApiResponseTime(responseTime, 2000);
                Assert.assertEquals(response.statusCode(), 200);
                Assert.assertTrue(responseTime < 2000);
                System.out.println("TC-API-03 PASSED - Response Time: " + responseTime + "ms");
        }
}