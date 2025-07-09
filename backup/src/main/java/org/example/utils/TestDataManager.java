package org.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TestDataManager {
    private static TestDataManager instance;
    private Properties properties;
    private Map<String, String> testDataMap;

    private TestDataManager() {
        properties = new Properties();
        testDataMap = new HashMap<>();
        loadProperties();
    }

    private void loadProperties() {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/testData.properties");
            properties.load(fis);
            for (String key : properties.stringPropertyNames()) {
                testDataMap.put(key, properties.getProperty(key));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading test data properties: " + e.getMessage());
        }
    }

    public static TestDataManager getInstance() {
        if (instance == null) {
            instance = new TestDataManager();
        }
        return instance;
    }

    public String getData(String key) {
        return testDataMap.get(key);
    }

    public Map<String, String> getUserData(String userNumber) {
        Map<String, String> userData = new HashMap<>();
        String prefix = "user" + userNumber + ".";
        for (String key : testDataMap.keySet()) {
            if (key.startsWith(prefix)) {
                String newKey = key.substring(prefix.length());
                userData.put(newKey, testDataMap.get(key));
            }
        }
        return userData;
    }

    public Map<String, String> getProductData(String productNumber) {
        Map<String, String> productData = new HashMap<>();
        String prefix = "product" + productNumber + ".";
        for (String key : testDataMap.keySet()) {
            if (key.startsWith(prefix)) {
                String newKey = key.substring(prefix.length());
                productData.put(newKey, testDataMap.get(key));
            }
        }
        return productData;
    }

    public void updateTestData(String key, String value) {
        testDataMap.put(key, value);
        // Update properties file if needed
    }

    public void addTestData(String key, String value) {
        testDataMap.put(key, value);
    }

    public void removeTestData(String key) {
        testDataMap.remove(key);
    }

    public boolean containsKey(String key) {
        return testDataMap.containsKey(key);
    }

    public void printAllData() {
        System.out.println("Test Data Map:");
        testDataMap.forEach((key, value) -> 
            System.out.println(key + " = " + value)
        );
    }
}
