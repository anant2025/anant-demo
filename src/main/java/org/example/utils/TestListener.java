package org.example.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
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
            driver = BaseClass.getDriver();
            if (driver != null) {
                // Generate timestamp and screenshot name
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String screenshotName = result.getName() + "_" + timestamp + ".png";
                
                // Get screenshots directory and ensure it exists
                String screenshotsDir = ReportManager.getScreenshotsDir();
                String screenshotPath = screenshotsDir + "/" + screenshotName;
                
                // Take screenshot
                try {
                    File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    File destFile = new File(screenshotPath);
                    FileUtils.copyFile(srcFile, destFile);
                    
                    // Log the screenshot in the report
                    ExtentTest extentTest = ReportManager.getTest();
                    if (extentTest != null) {
                        // Add exception details
                        extentTest.log(Status.FAIL, "Test Failed: " + result.getThrowable().getMessage());
                        
                        // Add screenshot using the enhanced method
                        ReportManager.addScreenshot(screenshotPath, "Failure Screenshot");
                        
                        // Store the relative path for the HTML report
                        String relativePath = "screenshots/" + screenshotName;
                        
                        // Store the screenshot path in test context for CustomReporter
                        result.setAttribute("screenshotPath", relativePath);
                        
                        // Also log in TestNG report with the correct relative path
                        Reporter.log("<a href='" + relativePath + "' target='_blank'><img src='" + relativePath + "' height='200' width='300'/></a>");
                    }
                } catch (Exception e) {
                    System.err.println("Error capturing screenshot: " + e.getMessage());
                    e.printStackTrace();
                }
                
            } else {
                System.err.println("Driver is null in TestListener.onTestFailure");
            }
        } catch (Exception e) {
            System.err.println("Error in onTestFailure: " + e.getMessage());
            e.printStackTrace();
            if (result != null) {
                result.setAttribute("screenshotError", e.getMessage());
            }
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
            extentTest.log(Status.PASS, "Test passed successfully!");
            
            // Optional: Add screenshot on success as well
            try {
                WebDriver driver = BaseClass.getDriver();
                if (driver != null) {
                    String screenshotsDir = System.getProperty("user.dir") + "/target/screenshots";
                    File dir = new File(screenshotsDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    
                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String screenshotName = "PASS_" + result.getName() + "_" + timestamp + ".png";
                    String screenshotPath = ReportManager.getScreenshotsDir() + "/" + screenshotName;
                    
                    try {
                        // Take screenshot
                        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                        File destFile = new File(screenshotPath);
                        FileUtils.copyFile(srcFile, destFile);
                        
                        // Add screenshot using the enhanced method
                        ReportManager.addScreenshot(screenshotPath, "Success Screenshot");
                        
                        // Store the relative path for the HTML report
                        String relativePath = "screenshots/" + screenshotName;
                        result.setAttribute("screenshotPath", relativePath);
                    } catch (Exception e) {
                        System.err.println("Error capturing success screenshot: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                extentTest.log(Status.INFO, "Could not capture success screenshot: " + e.getMessage());
            }
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
        // Flush the ExtentReports instance
        ReportManager.flush();
        
        // Generate test summary
        System.out.println("██     ██ ███████  ██████  ███    ██ ███████  ██████      ██     ██  ██████  ██████  ██      ██████  ██████  ██████  ██      \n" +
                "██     ██ ██      ██    ██ ████   ██ ██      ██   ██     ██     ██ ██    ██ ██   ██ ██      ██   ██ ██   ██ ██   ██ ██      \n" +
                "██  █  ██ █████   ██    ██ ██ ██  ██ █████   ██████      ██  █  ██ ██    ██ ██████  ██      ██████  ██████  ██████  ██      \n" +
                "██ ███ ██ ██      ██    ██ ██  ██ ██ ██      ██   ██     ██ ███ ██ ██    ██ ██   ██ ██      ██   ██ ██   ██ ██   ██ ██      \n" +
                " ███ ███  ███████  ██████  ██   ████ ███████ ██   ██      ███ ███   ██████  ██   ██ ███████ ██   ██ ██   ██ ██   ██ ███████ \n" +
                "                                                                                                                          \n" +
                "                 ========= WELCOME TO ANANT WORLD =========\n");

        System.out.println("TEST EXECUTION SUMMARY");
        System.out.println("Total Tests: " + (context.getPassedTests().size() + context.getFailedTests().size() + context.getSkippedTests().size()));
        System.out.println("Passed: " + context.getPassedTests().size());
        System.out.println("Failed: " + context.getFailedTests().size());
        System.out.println("Skipped: " + context.getSkippedTests().size());
        System.out.println("======================================\n");

        
        // Generate report path
        String reportPath = System.getProperty("user.dir") + "/target/reports/extent-report.html";
        System.out.println("Extent Report: " + reportPath);
    }
}
