package org.example.Feature_Test;

/**
 * LiciousAdminLoginTest contains test cases for Licious Admin login functionality
 * Features:
 * - Performs login using test data from properties file
 * - Verifies successful login by checking URL
 * - Uses BaseClass for WebDriver management
 * - Uses TestDataManager for test data handling
 */
import java.lang.Thread;
import org.example.pages.LoginPage;
import org.example.utils.BaseClass;
import org.example.utils.ReportManager;
import org.example.utils.TestDataManager;
import org.example.utils.TestBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OMS_MIGRATION extends TestBase {
    private LoginPage loginPage;
    private TestDataManager testDataManager;

    @Test(description = "Verify login with valid credentials")
    public void testLoginWithValidCredentials() {
        ReportManager.getTest().info("Starting testLoginWithValidCredentials.");
        // Get driver from BaseClass
        WebDriver driver = BaseClass.getDriver();
        ReportManager.getTest().info("WebDriver instance retrieved.");

        // Initialize TestDataManager
        testDataManager = TestDataManager.getInstance();

        // Get the login page URL from TestDataManager
        String baseUrl = testDataManager.getData("baseUrl");
        String expectedUrl = testDataManager.getData("expectedUrlAfterLogin");
        driver.get(baseUrl);
        ReportManager.getTest().info("Navigated to login page: " + baseUrl);

        // Initialize LoginPage
        loginPage = new LoginPage(driver);

        // Perform login using invalid data from TestDataManager
        loginPage.enterUsername(testDataManager.getData("login.username"));
        ReportManager.getTest().info("Entered username.");
        loginPage.enterPassword(testDataManager.getData("login.password"));
        ReportManager.getTest().info("Entered password.");
        loginPage.clickLoginButton();
        ReportManager.getTest().info("Clicked login button.");

        // Assert that the login fails as expected
//        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid login.");
//        ReportManager.getTest().info("Verified that an error message is displayed.");

        String currentUrl = driver.getCurrentUrl();
        wait.until(ExpectedConditions.urlToBe(expectedUrl));
        Assert.assertEquals(currentUrl, expectedUrl, "User should Move to login page.");
        ReportManager.getTest().info("Verified that the user should Move to login page.");
    }

    @Test(description = "validatedropdown")
    public void validatedropdown() throws InterruptedException {
        try {
            // Step 1: Open Catalog Management
            System.out.println("=== Starting Test: validatedropdown ===");
            loginPage.CatalogManagement();
            ReportManager.getTest().info("Successfully opened Catalog Management");
            
            // Step 2: Select City
            System.out.println("\n=== Selecting City ===");
            loginPage.City("City");
            ReportManager.getTest().info("Clicked on City");
            
            // Add a small delay to see the action
            Thread.sleep(2000);
            
            // Step 3: Create City
            System.out.println("\n=== Creating City ===");
            loginPage.CreateCity("Create City");
            ReportManager.getTest().info("Clicked on Create City");
            
            // Add a final delay to see the result
            Thread.sleep(3000);
            System.out.println("=== Test completed successfully ===");
            
        } catch (Exception e) {
            String errorMsg = "Test failed: " + e.getMessage();
            System.err.println(errorMsg);
            e.printStackTrace();
            ReportManager.getTest().fail(errorMsg);
            throw e;
        }
    }
}

