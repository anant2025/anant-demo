package org.example.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.io.File;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.ITestContext;
import org.testng.Reporter;

public class BaseClass {
    protected static WebDriver driver;
    protected static WebDriverWait wait;
    
    @BeforeSuite
    public void setup() {
        try {
            // Setup ChromeDriver
            WebDriverManager.chromedriver().setup();

            // Configure ChromeOptions
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            // Initialize driver
            driver = new ChromeDriver(options);

            // Set timeouts
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            
            // Set up explicit wait
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            
            // Initialize ExtentReports
            ReportManager.getInstance();
            
        } catch (Exception e) {
            System.err.println("Error in BaseClass setup: " + e.getMessage());
            if (driver != null) {
                driver.quit();
            }
            throw e;
        }
    }
    
    @BeforeMethod
    public void beforeMethod() {
        // Maximize window
        driver.manage().window().maximize();
    }
    
    public static WebDriver getDriver() {
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized. Check @BeforeSuite setup.");
        }
        return driver;
    }
    
    public static WebDriverWait getWait() {
        return wait;
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
            wait = null;
        }
    }
    
    @AfterSuite
    public void afterSuite() {
        if (driver != null) {
            driver.quit();
        }
    }
}
