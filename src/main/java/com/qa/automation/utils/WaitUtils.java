package com.qa.automation.utils;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.automation.config.ConfigReader;

public class WaitUtils {

    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver) {
        int waitSeconds = Integer.parseInt(
                ConfigReader.getProperty("explicitWait"));

        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(waitSeconds));
    }

    public WebElement waitForElementVisible(By locator) {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementClickable(By locator) {
        return wait.until(
                ExpectedConditions.elementToBeClickable(locator));
    }
}
