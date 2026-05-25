package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class PerformanceUtil {

    // checks if api response time is under limit
    public static void checkApiResponseTime(long responseTimeMs, long limitMs) {
        System.out.println("API Response Time: " + responseTimeMs + "ms");
        if (responseTimeMs > limitMs) {
            System.out.println("WARNING: Response time " + responseTimeMs + "ms exceeded limit of " + limitMs + "ms");
        } else {
            System.out.println("PASS: Response time is within limit");
        }
    }

    // measures how long page took to load using javascript
    public static long getPageLoadTime(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long loadTime = (Long) js.executeScript(
                "return performance.timing.loadEventEnd " +
                        "- performance.timing.navigationStart;");
        System.out.println("Page Load Time: " + loadTime + "ms");
        return loadTime;
    }

    // measures DOM ready time using javascript
    public static long getDomReadyTime(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long domTime = (Long) js.executeScript(
                "return performance.timing.domContentLoadedEventEnd " +
                        "- performance.timing.navigationStart;");
        System.out.println("DOM Ready Time: " + domTime + "ms");
        return domTime;
    }
}