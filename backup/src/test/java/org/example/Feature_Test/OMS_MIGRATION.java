package org.example.Feature_Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.pages.LoginPage;
import org.example.utils.ReportManager;
import org.example.utils.TestDataManager;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.lang.reflect.Method;

public class OMS_MIGRATION {
    protected static WebDriver driver;
    protected static final String SCREENSHOT_DIR = "test-output/screenshots/";
    private LoginPage loginPage;
    private TestDataManager testDataManager;
    private ExtentTest test;

    @BeforeSuite
    public void suiteSetup() {
        // Initialize TestDataManager
        testDataManager = TestDataManager.getInstance();
        
        // Create screenshots directory if it doesn't exist
        new File(SCREENSHOT_DIR).mkdirs();
        
        // Setup WebDriver
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }
    
    @BeforeMethod
    public void testSetup(Method method) {
        // Initialize test in ReportManager
        String testName = this.getClass().getSimpleName() + " - " + method.getName();
        test = ReportManager.createTest(testName);
        
        // Initialize LoginPage
        loginPage = new LoginPage(driver);
        
        // Navigate to login page before each test
        String baseUrl = testDataManager.getData("baseUrl");
        driver.get(baseUrl);
        test.log(Status.INFO, "Navigated to login page: " + baseUrl);
        
        // Accept cookies if present
        try {
            driver.findElement(By.id("cookie-consent-accept-all")).click();
            test.log(Status.INFO, "Accepted cookies");
        } catch (Exception e) {
            // Cookie banner not present, continue
        }
    }

    private String captureScreenshot(String screenshotName) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String screenshotPath = SCREENSHOT_DIR + screenshotName + "_" + timestamp + ".png";
            
            File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(screenshotPath);
            FileUtils.copyFile(srcFile, destFile);
            
            // Add screenshot to report
            if (test != null) {
                test.addScreenCaptureFromPath(screenshotPath);
            }
            return screenshotPath;
        } catch (Exception e) {
            if (test != null) {
                test.warning("Failed to capture screenshot: " + e.getMessage());
            }
            return "";
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            try {
                String screenshotPath = captureScreenshot("FAILURE_" + result.getName());
                if (!screenshotPath.isEmpty()) {
                    test.fail("Test failed. Screenshot: " + screenshotPath);
                } else {
                    test.fail("Test failed. Failed to capture screenshot.");
                }
            } catch (Exception e) {
                test.fail("Failed to capture screenshot: " + e.getMessage());
            }
        }
        
        // Flush the report at the end of each test
        ReportManager.flush();
    }
    
    @AfterMethod
    public void afterMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            try {
                String screenshotPath = captureScreenshot("FAILURE_" + result.getName());
                if (!screenshotPath.isEmpty()) {
                    test.fail("Test failed. Screenshot: " + screenshotPath);
                } else {
                    test.fail("Test failed. Failed to capture screenshot.");
                }
                // Log the exception
                if (result.getThrowable() != null) {
                    test.fail(result.getThrowable());
                }
            } catch (Exception e) {
                test.fail("Failed to capture screenshot: " + e.getMessage());
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.pass("Test passed successfully");
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.skip("Test was skipped");
        }
    }

    @AfterSuite
    public void suiteTearDown() {
        // Clean up WebDriver after all tests
        if (driver != null) {
            driver.quit();
            driver = null;
        }
        
        // Flush the report at the end of all tests
        ReportManager.flush();
    }

    @Test(description = "Verify login with valid credentials")
    public void testLoginWithValidCredentials() {
        try {
            // Test data
            String username = testDataManager.getData("validUsername");
            String password = testDataManager.getData("validPassword");
            String expectedUrl = testDataManager.getData("dashboardUrl");
            
            // Log test steps
            test.log(Status.INFO, "Starting test: Login with valid credentials");
            
            // Enter username
            loginPage.enterUsername(username);
            test.log(Status.INFO, "Entered username: " + username);
            
            // Enter password
            loginPage.enterPassword(password);
            test.log(Status.INFO, "Entered password");
            
            // Click login button
            loginPage.clickLoginButton();
            test.log(Status.INFO, "Clicked login button");
            
            // Wait for page to load and verify URL
            try {
                Thread.sleep(2000); // Simple wait, consider using WebDriverWait in production
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                test.log(Status.WARNING, "Thread interrupted while waiting for page to load");
            }
            String currentUrl = driver.getCurrentUrl();
            test.log(Status.INFO, "Current URL after login: " + currentUrl);
            
            // Verify successful login by checking URL
            Assert.assertTrue(currentUrl.contains(expectedUrl), "User should be redirected to dashboard after successful login");
            
            // Capture screenshot for verification
            String screenshotPath = captureScreenshot("successful_login");
            test.log(Status.INFO, "Screenshot captured: " + screenshotPath);
            
            test.log(Status.PASS, "Login with valid credentials test passed");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed with exception: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(description = "Verify login with invalid credentials")
    public void testLoginWithInvalidCredentials() {
        try {
            // Test data
            String username = "invalid_user@example.com";
            String password = "wrong_password";
            
            // Log test steps
            test.log(Status.INFO, "Starting test: Login with invalid credentials");
            
            // Enter username
            loginPage.enterUsername(username);
            test.log(Status.INFO, "Entered username: " + username);
            
            // Enter password
            loginPage.enterPassword(password);
            test.log(Status.INFO, "Entered password");
            
            // Click login button
            loginPage.clickLoginButton();
            test.log(Status.INFO, "Clicked login button");
            
            // Wait for error message
            try {
                Thread.sleep(1000); // Simple wait, consider using WebDriverWait in production
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                test.log(Status.WARNING, "Thread interrupted while waiting for error message");
            }
            
            // Verify error message is displayed
            boolean isErrorDisplayed = loginPage.isErrorMessageDisplayed();
            Assert.assertTrue(isErrorDisplayed, "Error message should be displayed for invalid credentials");
            
            // Capture screenshot for verification
            String screenshotPath = captureScreenshot("invalid_login_attempt");
            test.log(Status.INFO, "Screenshot captured: " + screenshotPath);
            
            test.log(Status.PASS, "Login with invalid credentials test passed");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed with exception: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(description = "Verify login with empty credentials")
    public void testLoginWithEmptyCredentials() {
        try {
            // Log test steps
            test.log(Status.INFO, "Starting test: Login with empty credentials");
            
            // Click login button without entering credentials
            loginPage.clickLoginButton();
            test.log(Status.INFO, "Clicked login button without entering credentials");
            
            // Verify error message is displayed
            boolean isErrorDisplayed = loginPage.isErrorMessageDisplayed();
            String errorMessage = loginPage.getErrorMessage();
            
            // Log test steps
            test.log(Status.INFO, "Verifying error message is displayed");
            test.log(Status.INFO, "Error message displayed: " + isErrorDisplayed);
            test.log(Status.INFO, "Error message text: " + errorMessage);
            
            // Assert error message is displayed
            Assert.assertTrue(isErrorDisplayed, "Error message should be displayed for empty credentials");
            Assert.assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
            
            // Capture screenshot for verification
            String screenshotPath = captureScreenshot("empty_credentials_validation");
            test.log(Status.INFO, "Screenshot captured: " + screenshotPath);
            
            test.log(Status.PASS, "Login with empty credentials test passed");
            
        } catch (AssertionError e) {
            // Capture screenshot on assertion failure
            String screenshotPath = captureScreenshot("login_empty_credentials_failure");
            test.log(Status.FAIL, "Test failed: " + e.getMessage() + "<br>Screenshot: <a href='" + screenshotPath + "'>View Screenshot</a>");
            throw e;
        } catch (Exception e) {
            // Capture screenshot on any other exception
            String screenshotPath = captureScreenshot("login_empty_credentials_error");
            test.log(Status.FAIL, "Unexpected error: " + e.getMessage() + "<br>Screenshot: <a href='" + screenshotPath + "'>View Screenshot</a>");
            throw new AssertionError("Test failed due to unexpected error", e);
        }
    }
    
    @Test(description = "Verify logout functionality")
    public void testLogout() {
        try {
            // First login with valid credentials
            testLoginWithValidCredentials();
            
            // Now test logout
            test.log(Status.INFO, "Starting test: Logout functionality");
            
            // Find and click logout button (adjust selector as per your application)
            driver.findElement(By.cssSelector("button[data-test='logout-button']")).click();
            test.log(Status.INFO, "Clicked logout button");
            
            // Wait for login page to load
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                test.log(Status.WARNING, "Thread interrupted while waiting for login page to load after logout");
            }
            
            // Verify we're back on the login page
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("auth/login"), "Should be redirected to login page after logout");
            
            // Capture screenshot for verification
            String screenshotPath = captureScreenshot("after_logout");
            test.log(Status.INFO, "Screenshot captured after logout: " + screenshotPath);
            
            test.log(Status.PASS, "Logout test passed");
            
        } catch (Exception e) {
            test.log(Status.FAIL, "Logout test failed with exception: " + e.getMessage());
            throw e;
        }
    }
}
