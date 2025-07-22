package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

import org.example.utils.WaitUtils;

import org.example.utils.BaseClass;

public class LoginPage {
    private WebDriver driver;
    private String currentUrl;

    // Page Elements
    private By usernameField = By.name("email");
    private By passwordField = By.name("password");
    private By loginButton = By.cssSelector("button[type='submit']");
    private By errorMessage = By.cssSelector(".error-message");
    private By CatalogManagement = By.xpath("//div[@class='page-sidebar navbar-collapse collapse']//li[contains(@class, 'nav-item') and not(ancestor::ul[contains(@class, 'sub-menu')]) and .//span[@class='title' and normalize-space(text())='Catalog Management']]");
    private By EditCity = By.xpath("/html/body/div[3]/div[2]/div/ul/li[55]/ul/li[1]/ul/li[1]/a/text()[2]");
    private By City = By.xpath("/html/body/div[3]/div[2]/div/ul/li[55]/ul/li[1]/ul/li[1]/a/text()[1]");
    private By Dropdown = By.xpath("//ul[@class = 'page-sidebar-menu  page-header-fixed scroll-ul']//li");

    public By getCityLocator() {
        return City;
    }

    // OR better, a method that handles waiting and clicking internally
    public void clickCity() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(City)).click();
    }



    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUsername(String username) {
        WaitUtils.waitForElementVisible(driver, usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        WaitUtils.waitForElementVisible(driver, passwordField).sendKeys(password);
    }

    public void clickLoginButton() {
        WaitUtils.waitForElementClickable(driver, loginButton).click();
        // Store the URL after login
        currentUrl = driver.getCurrentUrl();
    }
    public void CatalogManagement() {
        WaitUtils.waitForElementClickable(driver, CatalogManagement).click();
    }
    public void City() {
        WaitUtils.waitForElementClickable(driver, City).click();
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return BaseClass.getWait().until(ExpectedConditions.presenceOfElementLocated(errorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            return BaseClass.getWait().until(ExpectedConditions.presenceOfElementLocated(errorMessage)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isLoginSuccessful() {
        try {
            // Wait for URL to change to dashboard
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlContains("https://oms.qa.licious.app/"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Selects a city from the dropdown based on the provided city name.
     *
     * @param cityName The name of the city to select from the dropdown.
     */
    public void CtalogManagement(String ButtonName) {
        // Retrieve all items in the dropdown
        List<WebElement> items = driver.findElements(Dropdown);

        // Iterate through each item in the dropdown
        for (WebElement item : items) {
            // Get the text of the current dropdown item
            String text = item.getText().trim();
            System.out.println("Dropdown Item: " + text); // Log each item (optional)

            // Check if the current item matches the desired city name
            if (text.equalsIgnoreCase(ButtonName)) {
                // Click on the matching item
                item.click();
                System.out.println("Clicked on: " + text); // Log the clicked item
                break; // Exit the loop once the city is found and clicked
            }
        }
    }
    public void City(String ButtonName) {
        // Retrieve all items in the dropdown
        List<WebElement> items = driver.findElements(Dropdown);

        // Iterate through each item in the dropdown
        for (WebElement item : items) {
            // Get the text of the current dropdown item
            String text = item.getText().trim();
            System.out.println("Dropdown Item: " + text); // Log each item (optional)

            // Check if the current item matches the desired city name
            if (text.equalsIgnoreCase(ButtonName)) {
                // Click on the matching item
                item.click();
                System.out.println("Clicked on: " + text); // Log the clicked item
                break; // Exit the loop once the city is found and clicked
            }
        }

    }
    public void  CreateCity(String ButtonName) {
        // Retrieve all items in the dropdown
        List<WebElement> items = driver.findElements(Dropdown);

        // Iterate through each item in the dropdown
        for (WebElement item : items) {
            // Get the text of the current dropdown item
            String text = item.getText().trim();
            System.out.println("Dropdown Item: " + text); // Log each item (optional)

            // Check if the current item matches the desired city name
            if (text.equalsIgnoreCase(ButtonName)) {
                // Click on the matching item
                item.click();
                System.out.println("Clicked on: " + text); // Log the clicked item
                break; // Exit the loop once the city is found and clicked
            }
        }
    }
}
