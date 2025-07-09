package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.example.utils.WaitUtils;

public class LoginPage {
    private WebDriver driver;
    private String currentUrl;

    // Page Elements
    private By usernameField = By.name("email");
    private By passwordField = By.name("password");
    private By loginButton = By.cssSelector("button[type='submit']");
    private By errorMessage = By.cssSelector(".text-danger, .alert-danger, .error-message");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterUsername(String username) {
        System.out.println("Entering username: " + username);
        WaitUtils.waitForElementVisible(driver, usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        System.out.println("Entering password");
        WaitUtils.waitForElementVisible(driver, passwordField).sendKeys(password);
    }

    public void clickLoginButton() {
        System.out.println("Clicking login button");
        WaitUtils.waitForElementClickable(driver, loginButton).click();
        // Store the URL after login
        currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL after login button click: " + currentUrl);
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return WaitUtils.waitForElementVisible(driver, errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            return WaitUtils.waitForElementVisible(driver, errorMessage).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isLoginSuccessful() {
        try {
            // Wait for URL to change to dashboard
            return WaitUtils.waitForUrlContains(driver, "https://oms.qa.licious.app/", Duration.ofSeconds(10));
        } catch (Exception e) {
            return false;
        }
    }
}
