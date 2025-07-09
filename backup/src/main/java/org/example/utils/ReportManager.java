package org.example.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.io.File;

public class ReportManager {
    private static ExtentReports extent = null;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    
    public static void setTest(ExtentTest test) {
        ReportManager.test.set(test);
    }

    public static ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = "target/reports/extent-report.html";
            File reportDir = new File("target/reports");
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportPath);

            htmlReporter.config().setDocumentTitle("Licious Admin Test Report");
            htmlReporter.config().setReportName("Licious Admin Login Test Report");
            htmlReporter.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
        }
        return extent;
    }

    public static ExtentTest createTest(String testName) {
        ExtentReports extentReports = getInstance();
        ExtentTest testExtent = extentReports.createTest(testName);
        test.set(testExtent);
        return testExtent;
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void flush() {
        extent.flush();
    }

    public static void addScreenshot(String screenshotPath) {
        if (test.get() != null) {
            test.get().addScreenCaptureFromPath(screenshotPath);
        }
    }
}
