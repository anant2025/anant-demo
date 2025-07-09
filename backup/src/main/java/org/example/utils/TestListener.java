package org.example.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.ExtentTest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;

public class TestListener implements ITestListener {
    
    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = null;
        try {
            driver = BaseClass.getDriver();  // Get driver from BaseClass
            if (driver != null) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

                // Create screenshots directory if it doesn't exist
                String screenshotsDir = System.getProperty("user.dir") + "/target/screenshots";
                File dir = new File(screenshotsDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // Create screenshot file path
                String screenshotName = result.getName() + "_" + timestamp + ".png";
                String screenshotPath = screenshotsDir + "/" + screenshotName;

                // Take screenshot
                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                File targetFile = new File(screenshotPath);
                FileUtils.copyFile(screenshotFile, targetFile);

                // Add screenshot to test report
                System.out.println("Screenshot taken for failed test: " + result.getName() + " at " + screenshotPath);

                // Add screenshot to ExtentReports
                ExtentTest extentTest = ReportManager.getTest();
                if (extentTest != null) {
                    extentTest.log(Status.FAIL, "Test Failed with exception: " + result.getThrowable());
                    extentTest.log(Status.FAIL, "Screenshot below: " + extentTest.addScreenCaptureFromPath(screenshotPath));
                }

                // Add to test result attributes
                result.setAttribute("screenshotPath", screenshotPath);
            } else {
                System.err.println("Driver is null in TestListener.onTestFailure");
            }
        } catch (Exception e) {
            System.err.println("Error taking screenshot: " + e.getMessage());
            // Add error message to test result
            result.setAttribute("screenshotError", e.getMessage());
        }
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        ReportManager.createTest(result.getMethod().getMethodName());
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest extentTest = ReportManager.getTest();
        if (extentTest != null) {
            extentTest.log(Status.PASS, "Test Passed");
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest extentTest = ReportManager.getTest();
        if (extentTest != null) {
            extentTest.log(Status.SKIP, "Test Skipped");
        }
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not needed for screenshot functionality
    }
    
    @Override
    public void onStart(ITestContext context) {
        // Not needed for screenshot functionality
    }
    
    @Override
    public void onFinish(ITestContext context) {
        // Not needed for screenshot functionality
    }
}
