package org.example.utils;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Listeners;
import org.testng.ITestContext;
import java.io.File;

/**
 * TestBase class serves as the foundation for all test classes in the test automation framework.
 * It provides common setup and teardown methods that are executed before and after the test suite.
 *
 * Key Features:
 * - Initializes and manages the test environment
 * - Handles test reporting through ExtentReports
 * - Integrates TestNG listeners for test execution events
 * - Provides a centralized configuration point for all test classes
 *
 * Usage:
 * All test classes should extend this class to inherit the common test configuration and reporting capabilities.
 */
@Listeners(org.example.utils.TestListener.class)
public class TestBase extends BaseClass {

    /**
     * Executes once before the test suite starts.
     * - Calls parent class setup for basic test environment configuration
     * - Initializes the ExtentReports instance for test reporting
     */
    @BeforeSuite
    public void setup() {
        super.setup();
        // Initialize ExtentReports
        ReportManager.getInstance();
    }

    /**
     * Executes once after the test suite completes.
     * - Calls parent class teardown for resource cleanup
     * - Flushes ExtentReports to generate the final test report
     */
    @AfterSuite
    public void tearDown() {
        super.tearDown();
        // Flush ExtentReports
        ReportManager.getInstance().flush();
    }
}