package base;

import drivers.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import config.ConfigReader;
import utils.CSVReader;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected String email;
    protected String password;
    protected String noteTitle;
    protected String noteDescription;
    protected String noteCategory;

    @BeforeMethod
    public void setUp(Method method) {
        Map<String, String> loginData = CSVReader.getRowByType("valid_user");
        email = loginData.get("email");
        password = loginData.get("password");

        Map<String, String> notesData = CSVReader.getNotesRowByType("valid_note");
        noteTitle = notesData.get("noteTitle");
        noteDescription = notesData.get("noteDescription");
        noteCategory = notesData.get("noteCategory");

        String packageName = this.getClass().getPackage().getName();

        if (!packageName.contains("API")) {
            DriverManager.initDriver();
            driver = DriverManager.getDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            driver.get(ConfigReader.get("base.url"));
        }
    }

    @AfterMethod
    public void tearDown() {
        String packageName = this.getClass().getPackage().getName();
        if (!packageName.contains("API")) {
            DriverManager.quitDriver();
            System.out.println("[TEARDOWN] Browser closed for: " + this.getClass().getSimpleName());
        } else {
            System.out.println("[TEARDOWN] API test - no browser to close for: " + this.getClass().getSimpleName());
        }
    }
}