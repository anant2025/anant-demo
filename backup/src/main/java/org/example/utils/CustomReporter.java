package org.example.utils;

import org.testng.ITestResult;
import org.testng.Reporter;

public class CustomReporter {
    
    public static void reportScreenshot(ITestResult result) {
        if (result.getAttribute("screenshotPath") != null) {
            String screenshotPath = (String) result.getAttribute("screenshotPath");
            Reporter.log("<br><a href='" + screenshotPath + "' target='_blank'>" + 
                        "<img src='" + screenshotPath + "' height='200' width='300'/>" + 
                        "</a>", true);
        }
    }
}
