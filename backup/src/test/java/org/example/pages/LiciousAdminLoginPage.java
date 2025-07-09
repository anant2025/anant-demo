package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import org.example.utils.TestDataManager;

public class LiciousAdminLoginPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private TestDataManager testDataManager;

    // Page Elements
    private By usernameField = By.name("email");
    private By passwordField = By.name("password");
    private By loginButton = By.cssSelector("button[type='submit']");
    private By errorMessage = By.cssSelector(".error-message");
    private By rememberMe = By.cssSelector("input[type='checkbox']");

    public LiciousAdminLoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.testDataManager = TestDataManager.getInstance();
    }

    public void enterUsername(String username) {
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        usernameInput.clear();
        usernameInput.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickLoginButton() {
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginBtn.click();
    }

    public void clickRememberMe() {
        WebElement rememberMeCheckbox = wait.until(ExpectedConditions.elementToBeClickable(rememberMe));
        rememberMeCheckbox.click();
    }

    public String getErrorMessage() {
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return error.getText();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPageLoaded() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(usernameField));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginButtonEnabled() {
        WebElement loginBtn = wait.until(ExpectedConditions.presenceOfElementLocated(loginButton));
        return loginBtn.isEnabled();
    }

    public boolean isRememberMeChecked() {
        WebElement rememberMeCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(rememberMe));
        return rememberMeCheckbox.isSelected();
    }

    public void loginWithValidCredentials() {
        String username = testDataManager.getData("login.username");
        String password = testDataManager.getData("login.password");
        
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public void loginWithInvalidCredentials() {
        String invalidUsername = "invalid_user";
        String invalidPassword = "invalid_pass";
        
        enterUsername(invalidUsername);
        enterPassword(invalidPassword);
        clickLoginButton();
    }

    public void loginWithEmptyCredentials() {
        enterUsername("");
        enterPassword("");
        clickLoginButton();
    }
}
