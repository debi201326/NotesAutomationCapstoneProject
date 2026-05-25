package tests.UI;

import base.BaseTest;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WaitUtil;
import pages.LoginPage;
import utils.PerformanceUtil;
import utils.ScreenshotUtil;

public class LoginTest extends BaseTest {

    @Test(description = "TC-UI-01: Login with Valid Credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void TC_UI_01_ValidLogin() {
        try {
            System.out.println("===== TC-UI-01: Login with Valid Credentials =====");
            long pageLoadTime = PerformanceUtil.getPageLoadTime(driver);
            System.out.println("Page load time: " + pageLoadTime + "ms");
            LoginPage loginPage = new LoginPage(driver, wait);
            loginPage.login(email, password);
            boolean loaded = WaitUtil.waitForVisible(wait, By.cssSelector("[data-testid='logout']")).isDisplayed();
            ScreenshotUtil.captureAndAttach(driver, "TC-UI-01 Login Success");
            Assert.assertTrue(loaded, "Dashboard did not load after login");
            System.out.println("TC-UI-01 PASSED");

        } catch (Exception e) {
            ScreenshotUtil.captureAndAttach(driver, "TC-UI-01 FAILED");
            Assert.fail("TC-UI-01 Failed: " + e.getMessage());
        }
    }

}