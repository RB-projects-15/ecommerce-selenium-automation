package com.qa.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.automation.config.ConfigReader;
import com.qa.automation.data.User;

public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(ConfigReader.getBaseUrl());
        return waitUntilLoaded();
    }

    public LoginPage waitUntilLoaded() {
        waitVisible(LOGIN_BUTTON);
        return this;
    }

    public ProductsPage loginAs(User user) {
        return login(user.username(), user.password());
    }

    /** Logs in with credentials that are expected to succeed. */
    public ProductsPage login(String username, String password) {
        submitCredentials(username, password);
        return new ProductsPage(driver);
    }

    /** Logs in with credentials that are expected to be rejected; stays on the login page. */
    public LoginPage loginExpectingFailure(String username, String password) {
        submitCredentials(username, password);
        waitVisible(ERROR_MESSAGE);
        return this;
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    public boolean isLoginFormDisplayed() {
        return isDisplayed(LOGIN_BUTTON);
    }

    public boolean isPasswordMasked() {
        return "password".equals(waitVisible(PASSWORD).getAttribute("type"));
    }

    private void submitCredentials(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN_BUTTON);
    }
}
