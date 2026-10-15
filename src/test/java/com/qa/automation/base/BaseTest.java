package com.qa.automation.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.qa.automation.data.User;
import com.qa.automation.driver.DriverManager;
import com.qa.automation.pages.LoginPage;
import com.qa.automation.pages.ProductsPage;

/**
 * Base class for all UI tests.
 *
 * <p>Test classes must stay stateless: with {@code parallel="methods"} one instance is shared by
 * several threads, so the WebDriver is always fetched from the thread-local {@link DriverManager}
 * through {@link #driver()} rather than stored in a field.
 */
public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    protected LoginPage openLoginPage() {
        return new LoginPage(driver()).open();
    }

    protected ProductsPage loginAsStandardUser() {
        return openLoginPage().loginAs(User.STANDARD);
    }
}
