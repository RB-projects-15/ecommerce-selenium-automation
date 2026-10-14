package com.qa.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.automation.utils.WaitUtils;

public class LoginPage {

    private final WaitUtils wait;

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.wait = new WaitUtils(driver);
    }

    public void enterUsername(String username) {
        wait.waitForElementVisible(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        wait.waitForElementVisible(passwordField).sendKeys(password);
    }

    public void clickLoginButton() {
        wait.waitForElementClickable(loginButton).click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public String getErrorMessage() {
        return wait.waitForElementVisible(errorMessage).getText();
    }
}
