package com.qa.automation.base;

import com.qa.automation.driver.DriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import com.qa.automation.utils.TestListener;

import java.nio.file.Files;
import java.nio.file.Path;

@Listeners(TestListener.class)
public class BaseTest {

    public WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverManager.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        // Save a screenshot if the test failed, before the browser closes
        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            try {
                byte[] png = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);
                Path dir = Path.of("target", "screenshots");
                Files.createDirectories(dir);
                Files.write(dir.resolve(result.getName() + ".png"), png);
            } catch (Exception e) {
                System.out.println("Could not save screenshot: " + e.getMessage());
            }
        }

        DriverManager.quitDriver();
    }
}