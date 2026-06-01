package tests.API;

import base.BaseTest;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;
import utils.JMeterUtil;

public class JMeterTest extends BaseTest {

    private static final String PLAN_30    = "src/test/resources/jmeter/testplan_30_users.jmx";
    private static final String PLAN_50    = "src/test/resources/jmeter/testplan_50_users.jmx";
    private static final String RESULT_30  = "target/jmeter/30_users_results.jtl";
    private static final String RESULT_50  = "target/jmeter/50_users_results.jtl";
    private static final String REPORT_30  = "target/jmeter/report_30users";
    private static final String REPORT_50  = "target/jmeter/report_50users";

    @Test(description = "Performance Test - 30 Users")
    @Severity(SeverityLevel.NORMAL)
    public void TC_PERF_01_30Users() throws Exception {
        System.out.println("TC-PERF-01 : 30 USERS TEST");
        JMeterUtil.runJMeter(PLAN_30, RESULT_30, REPORT_30);
        JMeterUtil.printPerformanceSummary(RESULT_30, "30 USERS");
        System.out.println("TC-PERF-01 PASSED");
    }

    @Test(description = "Performance Test - 50 Users")
    @Severity(SeverityLevel.NORMAL)
    public void TC_PERF_02_50Users() throws Exception {
        System.out.println("TC-PERF-02 : 50 USERS TEST");
        JMeterUtil.runJMeter(PLAN_50, RESULT_50, REPORT_50);
        JMeterUtil.printPerformanceSummary(RESULT_50, "50 USERS");
        System.out.println("TC-PERF-02 PASSED");
    }
}