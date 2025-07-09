package org.example.utils;

/**
 * ScreenShotUtils provides functionality to capture screenshots during test execution
 * Features:
 * - Takes screenshots of the browser window
 * - Automatically generates timestamps for screenshot filenames
 * - Stores screenshots in a dedicated directory
 * - Handles screenshot file creation and storage
 */
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;

public class ScreenShotUtils {
    
    /**
     * Directory where screenshots will be stored
     */
    private static final String SCREENSHOTS_DIR = "target/screenshots";
    
    static {
        // Create screenshots directory if it doesn't exist
        new File(SCREENSHOTS_DIR).mkdirs();
    }

    /**
     * Takes a screenshot of the current browser window
     * 
     * @param driver WebDriver instance to take screenshot from
     * @param testName Name of the test for which screenshot is taken
     * 
     * Process:
     * 1. Generates a timestamp for unique filename
     * 2. Creates filename using test name and timestamp
     * 3. Takes screenshot using WebDriver's TakesScreenshot interface
     * 4. Saves screenshot to target/screenshots directory
     * 5. Logs the path where screenshot was saved
     * 
     * @throws IOException if there's an error saving the screenshot
     */
    public static void takeScreenshot(WebDriver driver, String testName) {
        try {
            // Get current timestamp
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            
            // Create screenshot filename
            String screenshotName = testName + "_" + timestamp + ".png";
            
            // Take screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            
            // Save screenshot to target/screenshots directory
            File targetFile = new File(SCREENSHOTS_DIR + File.separator + screenshotName);
            FileUtils.copyFile(screenshot, targetFile);
            
            System.out.println("Screenshot saved: " + targetFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error taking screenshot: " + e.getMessage());
        }
    }
}
