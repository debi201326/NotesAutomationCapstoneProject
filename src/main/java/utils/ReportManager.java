package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ReportManager implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private static synchronized void initExtent() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter(
                "target/extent-report/ExtentReport.html");
            spark.config().setReportName("Notes App - Full Test Report");
            spark.config().setDocumentTitle("Test Execution Report");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("App", "Notes App - ExpandTesting");
            extent.setSystemInfo("Browser", "Chrome");
            extent.setSystemInfo("Environment", "QA");
        }
    }

    @Override
    public void onStart(ITestContext context) {
        initExtent();
    }

    @Override
    public void onTestStart(ITestResult result) {
        initExtent();
        ExtentTest extentTest = extent.createTest(
            result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test Passed");
        extent.flush();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail(result.getThrowable());

        try {
            Object instance = result.getInstance();
            WebDriver driver = (WebDriver) instance.getClass()
                .getField("driver").get(instance);
            String base64 = ((org.openqa.selenium.TakesScreenshot) driver)
                .getScreenshotAs(org.openqa.selenium.OutputType.BASE64);
            test.get().addScreenCaptureFromBase64String(base64, "Failure Screenshot");
            ScreenshotUtil.captureAndAttach(driver,
                "Failure - " + result.getMethod().getMethodName());
        } catch (Exception e) {
            test.get().warning("Could not capture screenshot: " + e.getMessage());
        }

        extent.flush();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("Test Skipped");
        extent.flush();
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
            System.out.println("Extent Report saved at: target/extent-report/ExtentReport.html");
        }
    }
}