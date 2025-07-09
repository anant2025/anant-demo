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
        // Setup ChromeDriver
        WebDriverManager.chromedriver().setup();

        // Configure ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");

        // Initialize driver
        driver = new ChromeDriver(options);

        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        
        // Set up wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
