package com.qa.automation.driver;

import com.qa.automation.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverManager() {
        // Prevent object creation
    }

    public static WebDriver getDriver() {

        if (driver.get() == null) {

            String browser = ConfigReader
                    .getProperty("browser")
                    .trim()
                    .toLowerCase();

            WebDriver webDriver;

            switch (browser) {

                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();

                    if (System.getenv("CI") != null) {
                        chromeOptions.addArguments("--headless");
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                    }

                    webDriver = new ChromeDriver(chromeOptions);
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();

                    if (System.getenv("CI") != null) {
                        firefoxOptions.addArguments("-headless");
                    }

                    webDriver = new FirefoxDriver(firefoxOptions);
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Unsupported browser: " + browser);
            }

            webDriver.manage().window().maximize();
            driver.set(webDriver);
        }

        return driver.get();
    }

    public static void quitDriver() {

        WebDriver webDriver = driver.get();

        if (webDriver != null) {
            try {
                webDriver.quit();
            } finally {
                driver.remove();
            }
        }
    }
}
