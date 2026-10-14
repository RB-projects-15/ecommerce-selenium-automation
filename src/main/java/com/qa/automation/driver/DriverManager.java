package com.qa.automation.driver;

import com.qa.automation.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverManager {

    private static WebDriver driver;

    private DriverManager() {
        // Prevent object creation
    }

    public static WebDriver getDriver() {

        if (driver == null) {

            String browser = ConfigReader
                    .getProperty("browser")
                    .trim()
                    .toLowerCase();

            switch (browser) {
                case "chrome":
                    driver = new ChromeDriver();
                    break;

                case "firefox":
                    driver = new FirefoxDriver();
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Unsupported browser: " + browser);
            }

            driver.manage().window().maximize();
        }

        return driver;
    }

    public static void quitDriver() {

        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
