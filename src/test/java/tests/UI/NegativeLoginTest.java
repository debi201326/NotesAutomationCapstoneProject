package tests.UI;

import base.BaseTest;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import java.util.Map;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.WaitUtil;
import pages.LoginPage;
import utils.AdHandler;
import utils.ClickUtil;
import utils.ScreenshotUtil;
import utils.CSVReader;

public class NegativeLoginTest extends BaseTest {

        @Test(description = "TC-NEG-06: Login with Wrong Password")
        @Severity(SeverityLevel.NORMAL)
        public void TC_NEG_06_WrongPassword() {
                try {
                        System.out.println("TC-NEG-06: Login with Wrong Password");
                        Map<String, String> invalidData = CSVReader.getRowByType("invalid_user");
                        String wrongEmail = invalidData.get("email");
                        String wrongPassword = invalidData.get("password");
                        LoginPage loginPage = new LoginPage(driver, wait);
                        loginPage.loginWithoutWait(wrongEmail, wrongPassword);
                        Thread.sleep(2000);
                        String pageSource = driver.getPageSource();
                        boolean errorShown = pageSource.contains("Incorrect");
                        ScreenshotUtil.captureAndAttach(driver, "TC-NEG-06 Wrong Password Error");
                        Assert.assertTrue(errorShown, "Error message not shown for wrong password");
                        System.out.println("TC-NEG-06 PASSED");

                } catch (Exception e) {
                        ScreenshotUtil.captureAndAttach(driver, "TC-NEG-06 FAILED");
                        Assert.fail("TC-NEG-06 Failed: " + e.getMessage());
                }
        }

        @Test(description = "TC-NEG-07: Login with Both Fields Empty")
        @Severity(SeverityLevel.NORMAL)
        public void TC_NEG_07_BothFieldsEmpty() {

                try {
                        System.out.println("TC-NEG-07: Login with Both Fields Empty");
                        AdHandler.dismissAd(driver);
                        ClickUtil.click(driver, wait, By.linkText("Login"));
                        WaitUtil.waitForVisible(wait, By.id("email"));
                        AdHandler.dismissAd(driver);
                        ClickUtil.click(driver, wait, By.xpath("//button[text()='Login']"));
                        Thread.sleep(1500);
                        boolean validationShown = WaitUtil.waitForVisible(wait, By.className("invalid-feedback")).isDisplayed();
                        ScreenshotUtil.captureAndAttach(driver, "TC-NEG-07 Both Fields Empty");
                        Assert.assertTrue(validationShown, "Validation not shown when both fields are empty");
                        System.out.println("TC-NEG-07 PASSED");

                } catch (Exception e) {
                        ScreenshotUtil.captureAndAttach(driver, "TC-NEG-07 FAILED");
                        Assert.fail("TC-NEG-07 Failed: " + e.getMessage());
                }
        }

}