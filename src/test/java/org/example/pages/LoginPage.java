package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.example.utils.WaitUtils;

public class LoginPage {
    private final WebDriver driver;
    private String currentUrl;

    // Page Elements
    private final By usernameField = By.name("email");
    private final By passwordField = By.name("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By CatalogManagement = By.xpath("//a[@class='nav-link nav-toggle' and span[@class='title' and text()='Catalog Management']]");
    private final By City = By.xpath("/html/body/div[3]/div[2]/div/ul/li[55]/ul/li[1]/ul/li[1]/a/text()[1]");

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
        currentUrl = driver.getCurrentUrl();
    }

    public void CatalogManagement() {
        try {
            System.out.println("=== Opening Catalog Management ===");
            
            // Wait for the menu to be clickable
            WebElement catalogMenu = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(CatalogManagement));
            
            // Log the current state
            System.out.println("Element found. Tag: " + catalogMenu.getTagName() + 
                           ", Text: " + catalogMenu.getText() +
                           ", Displayed: " + catalogMenu.isDisplayed() +
                           ", Enabled: " + catalogMenu.isEnabled());
            
            // Scroll into view and click using JavaScript
            ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", catalogMenu);
            Thread.sleep(500);
            
            // Click using JavaScript
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();", catalogMenu);
            System.out.println("Clicked on Catalog Management");
            
            // Wait for the menu to expand
            Thread.sleep(2000);
            
            // Verify if the menu expanded by checking for a child element
            try {
                WebElement subMenu = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//a[contains(@class, 'nav-link nav-toggle') and .//span[text()='Catalog Management']]/following-sibling::ul")));
                System.out.println("Catalog Management menu expanded successfully");
            } catch (Exception e) {
                System.out.println("Warning: Could not verify menu expansion. Continuing...");
            }
            
        } catch (Exception e) {
            System.out.println("Error in CatalogManagement: " + e.getMessage());
            
            // Take a screenshot for debugging
            try {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String screenshotPath = System.getProperty("user.dir") + "/screenshots/catalog_error_" + System.currentTimeMillis() + ".png";
                FileUtils.copyFile(screenshot, new File(screenshotPath));
                System.out.println("Screenshot saved to: " + screenshotPath);
            } catch (Exception screenshotEx) {
                System.out.println("Failed to take screenshot: " + screenshotEx.getMessage());
            }
            
            throw new RuntimeException("Failed to open Catalog Management menu", e);
        }
    }

    public String getCurrentUrl() {
        return currentUrl;
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

    public void City(String ButtonName) {
        try {
            System.out.println("=== Starting City Selection ===");
            System.out.println("Looking for city: " + ButtonName);
            
            // First, find and expand the Catalog Management menu if not already expanded
            WebElement catalogMenu = driver.findElement(By.xpath("//span[contains(text(),'Catalog Management')]/ancestor::li"));
            System.out.println("Catalog menu found. Current state: " + catalogMenu.getAttribute("class"));
            
            // Check if the menu is not already open
            if (!catalogMenu.getAttribute("class").contains("open")) {
                System.out.println("Expanding Catalog Management menu...");
                catalogMenu.click();
                Thread.sleep(1500); // Wait for animation
            }
            
            // Wait for the submenu to be visible
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Try to find the city link with more specific XPath
            String cityXpath = String.format("//span[text()='Catalog Management']/ancestor::li//a[contains(.,'%s')]", ButtonName);
            System.out.println("Looking for city with XPath: " + cityXpath);
            
            // Wait for the city element to be present and visible
            WebElement cityElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(cityXpath)));
            System.out.println("City element found. Text: " + cityElement.getText());
            
            // Scroll to the element using JavaScript
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", cityElement);
            Thread.sleep(1000);
            
            // Try to click using JavaScript directly
            System.out.println("Attempting JavaScript click...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cityElement);
            System.out.println("JavaScript click executed");
            
            // Verify if the click was successful by checking for a change in the UI
            try {
                wait.until(ExpectedConditions.stalenessOf(cityElement));
                System.out.println("Successfully navigated after clicking: " + ButtonName);
            } catch (Exception e) {
                System.out.println("Warning: May not have navigated after clicking. " + e.getMessage());
            }
            
            // Add a small delay to observe the action
            Thread.sleep(2000);
            
        } catch (Exception e) {
            System.out.println("!!! ERROR in City method: " + e.getMessage());
            e.printStackTrace();
            
            // Take a screenshot for debugging
            try {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String screenshotPath = System.getProperty("user.dir") + "/screenshots/error_" + System.currentTimeMillis() + ".png";
                FileUtils.copyFile(screenshot, new File(screenshotPath));
                System.out.println("Screenshot saved to: " + screenshotPath);
            } catch (Exception screenshotEx) {
                System.out.println("Failed to take screenshot: " + screenshotEx.getMessage());
            }
        }

    }
    public void CreateCity(String ButtonName) {
        try {
            System.out.println("=== Starting Create City Selection ===");
            System.out.println("Looking for Create City option: " + ButtonName);
            
            // Find the Create City element using XPath
            String createCityXPath = String.format("//a[contains(.,'%s')]", ButtonName);
            WebElement createCityElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(createCityXPath)));
            
            // Scroll to the element
            scrollToElement(createCityElement);
            
            // Click using JavaScript
            ((JavascriptExecutor)driver).executeScript("arguments[0].click();", createCityElement);
            System.out.println("Clicked on: " + ButtonName);
            
            // Wait for the action to complete
            Thread.sleep(2000);
            
        } catch (Exception e) {
            System.out.println("Error in CreateCity: " + e.getMessage());
            
            // Take a screenshot for debugging
            try {
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String screenshotPath = System.getProperty("user.dir") + "/screenshots/create_city_error_" + System.currentTimeMillis() + ".png";
                FileUtils.copyFile(screenshot, new File(screenshotPath));
                System.out.println("Screenshot saved to: " + screenshotPath);
            } catch (Exception screenshotEx) {
                System.out.println("Failed to take screenshot: " + screenshotEx.getMessage());
            }
            
            throw new RuntimeException("Failed to click on Create City: " + ButtonName, e);
        }
    }

    private void scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})", element);
            Thread.sleep(500);
        } catch (Exception e) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                Thread.sleep(300);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
