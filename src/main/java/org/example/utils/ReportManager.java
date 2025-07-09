package org.example.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

public class ReportManager {
    private static ExtentReports extent = null;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static void setTest(ExtentTest test) {
        ReportManager.test.set(test);
    }

    public static ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/target/reports/extent-report.html";
            File reportDir = new File("target/reports");
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportPath);
            
            // Basic configuration with custom title and styling
            htmlReporter.config().setDocumentTitle("Licious - Automation Test Report");
            htmlReporter.config().setReportName("<div style='font-size: 24px;background-color: red; color: white; padding: 10px; font-weight: bold;'>LICIOUS</div>");
            htmlReporter.config().setTheme(Theme.STANDARD);
            
            // Additional configuration for better reporting
            htmlReporter.config().setEncoding("utf-8");
            htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
            
            // Add custom JavaScript for Licious branding and layout
            String customJS = 
                "document.addEventListener('DOMContentLoaded', function() {" +
                "  // Add Licious header" +
                "  var header = document.createElement('div');" +
                "  header.style.backgroundColor = '#e91e63';" +
                "  header.style.padding = '15px 20px';" +
                "  header.style.color = 'white';" +
                "  header.style.display = 'flex';" +
                "  header.style.alignItems = 'center';" +
                "  var title = document.createElement('h1');" +
                "  title.style.margin = '0';" +
                "  title.style.fontSize = '24px';" +
                "  var liciousSpan = document.createElement('span');" +
                "  liciousSpan.textContent = 'LICIOUS';" +
                "  liciousSpan.style.fontWeight = 'bold';" +
                "  var subSpan = document.createElement('span');" +
                "  subSpan.textContent = 'Test Automation';" +
                "  subSpan.style.marginLeft = '10px';" +
                "  subSpan.style.fontSize = '18px';" +
                "  title.appendChild(liciousSpan);" +
                "  title.appendChild(subSpan);" +
                "  header.appendChild(title);" +
                "  document.body.insertBefore(header, document.body.firstChild);" +
                "  " +
                "  // Add test cases heading" +
                "  var testCasesHeader = document.createElement('h2');" +
                "  testCasesHeader.textContent = 'Test Cases';" +
                "  testCasesHeader.style.color = '#e91e63';" +
                "  testCasesHeader.style.borderBottom = '2px solid #e91e63';" +
                "  testCasesHeader.style.paddingBottom = '8px';" +
                "  testCasesHeader.style.margin = '20px 0';" +
                "  var testView = document.querySelector('.test-wrapper.test-level');" +
                "  if (testView) testView.prepend(testCasesHeader);" +
                "});";
            
            // Apply the custom JavaScript
            try {
                htmlReporter.config().setJs(customJS);
            } catch (Exception e) {
                System.err.println("Warning: Could not set custom JavaScript: " + e.getMessage());
            }
            
            // Set CSS for better report styling with Licious theme
            try {
                htmlReporter.config().setCss(
                    ".nav-wrapper { background-color: #e91e63 !important; width: 250px !important; } " +
                    ".brand-logo { padding: 15px 10px !important; text-align: center; } " +
                    ".blue { background-color: #e91e63 !important; } " +
                    ".nav-version { color: white !important; } " +
                    ".brand-logo img { height: 40px !important; } " +
                    ".main-panel { margin-left: 250px !important; } " +
                    "@media (max-width: 768px) { " +
                    "  .nav-wrapper { width: 100% !important; height: auto !important; position: relative !important; } " +
                    "  .main-panel { margin-left: 0 !important; margin-top: 60px !important; } " +
                    "  .brand-logo { text-align: left !important; padding: 10px !important; } " +
                    "}"
                );
            } catch (Exception e) {
                System.err.println("Warning: Could not set custom CSS: " + e.getMessage());
            }
            
            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            
            // System Info
            extent.setSystemInfo("Organization", "Licious");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
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

    /**
     * Adds a screenshot to the test report by embedding it directly as base64
     * @param screenshotPath Absolute path to the screenshot file
     * @param title Title for the screenshot in the report
     */
    public static void addScreenshot(String screenshotPath, String title) {
        if (test.get() == null || screenshotPath == null || screenshotPath.isEmpty()) {
            return;
        }
        
        try {
            // Read the screenshot file as bytes
            File file = new File(screenshotPath);
            if (!file.exists()) {
                System.err.println("Screenshot file not found: " + screenshotPath);
                return;
            }
            
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String base64String = Base64.getEncoder().encodeToString(fileContent);
            
            // Create an HTML image tag with the base64-encoded image
            String html = String.format(
                "<div style='margin:10px 0;'>" +
                "<h4>%s</h4>" +
                "<a href='data:image/png;base64,%s' target='_blank'>" +
                "<img src='data:image/png;base64,%s' style='max-width:100%%; border:1px solid #ddd; border-radius:4px; padding:5px;'/>" +
                "</a></div>",
                title, base64String, base64String
            );
            
            // Add the HTML to the report
            test.get().info(html);
            
        } catch (Exception e) {
            System.err.println("Error adding screenshot to report: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to the original method if base64 encoding fails
            try {
                test.get().addScreenCaptureFromPath(screenshotPath, title);
            } catch (Exception ex) {
                test.get().warning("Failed to add screenshot: " + ex.getMessage());
            }
        }
    }
    
    /**
     * Converts an absolute path to a relative path for the HTML report
     */
    private static String getRelativePath(String absolutePath) {
        try {
            String basePath = new File(System.getProperty("user.dir")).getCanonicalPath();
            String normalizedPath = new File(absolutePath).getCanonicalPath();
            
            if (normalizedPath.startsWith(basePath)) {
                return "." + normalizedPath.substring(basePath.length()).replace("\\", "/");
            }
            
            // If we can't make it relative, try to find the screenshots directory
            int screenshotsIndex = normalizedPath.indexOf("screenshots");
            if (screenshotsIndex > 0) {
                return "./screenshots/" + new File(normalizedPath).getName();
            }
            
            return absolutePath; // Fallback to original path
        } catch (Exception e) {
            System.err.println("Error getting relative path: " + e.getMessage());
            return absolutePath;
        }
    }
    
    /**
     * Gets the screenshots directory, creates it if it doesn't exist
     */
    public static String getScreenshotsDir() {
        String screenshotsDir = System.getProperty("user.dir") + "/target/screenshots";
        File dir = new File(screenshotsDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return screenshotsDir;
    }
}
