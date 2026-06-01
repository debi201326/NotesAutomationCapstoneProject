package utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import config.ConfigReader;

public class ScreenshotUtil {
    private static final String SCREENSHOT_DIR = ConfigReader.get("screenshot_dir");

    public static void captureAndAttach(WebDriver driver, String name) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, new ByteArrayInputStream(screenshot));
            System.out.println("Screenshot attached to Allure: " + name);
            saveToFolder(screenshot, name);

        } catch (Exception e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
    }

    private static void saveToFolder(byte[] screenshot, String name) {
        try {
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String safeName = name.replaceAll("[^a-zA-Z0-9_\\-]", "_");
            String filePath = SCREENSHOT_DIR + safeName + "_" + timestamp + ".png";
            try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
                fos.write(screenshot);
            }
            System.out.println("Screenshot saved to: " + filePath);

        } catch (Exception e) {
            System.out.println("Could not save screenshot to folder: " + e.getMessage());
        }
    }
}