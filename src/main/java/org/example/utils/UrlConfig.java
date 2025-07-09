package org.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UrlConfig {
    private static final Properties props = new Properties();
    private static final String PROPERTIES_FILE = "urls.properties";

    static {
        try {
            InputStream inputStream = UrlConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            if (inputStream != null) {
                props.load(inputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load URL configuration", e);
        }
    }

    public static String getBaseUrl() {
        return props.getProperty("baseUrl");
    }

    public static String getSearchUrl() {
        return props.getProperty("searchUrl");
    }

    public static String getMapsUrl() {
        return props.getProperty("mapsUrl");
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }
}
