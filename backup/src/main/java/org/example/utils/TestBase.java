package org.example.utils;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Listeners;
import org.testng.ITestContext;
import java.io.File;

@Listeners(org.example.utils.TestListener.class)
public class TestBase extends BaseClass {
    
    @BeforeSuite
    public void setup() {
        super.setup();
        // Initialize ExtentReports
        ReportManager.getInstance();
    }

    @AfterSuite
    public void tearDown() {
        super.tearDown();
        // Flush ExtentReports
        ReportManager.getInstance().flush();
    }
}
