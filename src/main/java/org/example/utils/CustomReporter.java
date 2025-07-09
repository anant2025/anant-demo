package org.example.utils;

import org.testng.ITestResult;
import org.testng.Reporter;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.ExtentTest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * CustomReporter provides enhanced reporting capabilities for test execution
 * including screenshot capture and test status reporting.
 */
public class CustomReporter {
    
    /**
     * Logs a screenshot in the test report
     * @param result The test result containing screenshot information
     */
    public static void reportScreenshot(ITestResult result) {
        try {
            if (result.getAttribute("screenshotPath") != null) {
                String screenshotPath = (String) result.getAttribute("screenshotPath");
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                
                // Log to TestNG report
                String screenshotLink = String.format(
                    "<br><div style='margin: 10px 0;'><b>%s - Screenshot:</b><br>" +
                    "<a href='%s' target='_blank'>" +
                    "<img src='%s' height='300' width='500' style='border:1px solid #ddd;'/>" +
                    "</a></div>",
                    timestamp,
                    screenshotPath,
                    screenshotPath
                );
                
                Reporter.log(screenshotLink);
                
                // Also log to Extent Report if needed
                ExtentTest extentTest = ReportManager.getTest();
                if (extentTest != null) {
                    extentTest.log(Status.INFO, "Screenshot captured at: " + timestamp);
                    extentTest.addScreenCaptureFromPath(screenshotPath, "Screenshot");
                }
            }
        } catch (Exception e) {
            System.err.println("Error in CustomReporter: " + e.getMessage());
            Reporter.log("<br>Failed to attach screenshot: " + e.getMessage());
        }
    }
    
    /**
     * Logs a custom message to both TestNG and Extent reports
     * @param message The message to log
     * @param status The status of the message (PASS, FAIL, INFO, etc.)
     */
    public static void log(String message, Status status) {
        // Log to TestNG report
        String formattedMessage = String.format("<div style='margin: 5px 0; padding: 5px; background-color: %s; color: white;'>%s</div>",
            getStatusColor(status), message);
        Reporter.log(formattedMessage);
        
        // Log to Extent report
        ExtentTest extentTest = ReportManager.getTest();
        if (extentTest != null) {
            extentTest.log(status, message);
        }
    }
    
    /**
     * Gets the appropriate background color based on status
     */
    private static String getStatusColor(Status status) {
        switch(status) {
            case PASS: return "#4CAF50"; // Green
            case FAIL: return "#f44336"; // Red
            case SKIP: return "#FF9800"; // Orange
            case WARNING: return "#FFC107"; // Yellow
            default: return "#2196F3"; // Blue
        }
    }
}
