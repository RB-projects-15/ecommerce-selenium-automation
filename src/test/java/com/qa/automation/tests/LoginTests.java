package com.qa.automation.tests;

import static com.qa.automation.base.TestGroups.NEGATIVE;
import static com.qa.automation.base.TestGroups.REGRESSION;
import static com.qa.automation.base.TestGroups.SMOKE;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.automation.base.BaseTest;
import com.qa.automation.config.ConfigReader;
import com.qa.automation.data.LoginDataProvider;
import com.qa.automation.data.TestData;
import com.qa.automation.data.User;
import com.qa.automation.pages.LoginPage;
import com.qa.automation.pages.ProductsPage;

public class LoginTests extends BaseTest {

    @Test(groups = { SMOKE, REGRESSION }, description = "A valid user can log in and lands on the Products page")
    public void verifyStandardUserCanLogIn() {
        ProductsPage productsPage = openLoginPage().loginAs(User.STANDARD);

        Assert.assertTrue(productsPage.getCurrentUrl().contains("inventory.html"),
                "User should be redirected to the inventory page after login.");
        Assert.assertEquals(productsPage.getTitle(), "Products", "Products page title is incorrect.");
    }

    @Test(groups = { REGRESSION, NEGATIVE },
            dataProvider = "invalidCredentials", dataProviderClass = LoginDataProvider.class,
            description = "Invalid or missing credentials are rejected with a clear error")
    public void verifyLoginFailsWithInvalidCredentials(String username, String password, String expectedError) {
        LoginPage loginPage = openLoginPage().loginExpectingFailure(username, password);

        Assert.assertTrue(loginPage.getErrorMessage().contains(expectedError),
                "Unexpected error message: " + loginPage.getErrorMessage());
        Assert.assertFalse(loginPage.getCurrentUrl().contains("inventory.html"),
                "User must not reach the inventory page with invalid credentials.");
    }

    @Test(groups = { REGRESSION, NEGATIVE }, description = "A locked-out user cannot log in")
    public void verifyLockedOutUserCannotLogIn() {
        LoginPage loginPage = openLoginPage()
                .loginExpectingFailure(User.LOCKED_OUT.username(), TestData.PASSWORD);

        Assert.assertTrue(loginPage.getErrorMessage().contains("this user has been locked out"),
                "Unexpected error message: " + loginPage.getErrorMessage());
    }

    @Test(groups = { REGRESSION, NEGATIVE }, description = "Protected pages cannot be opened without logging in")
    public void verifyInventoryIsNotAccessibleWithoutLogin() {
        openLoginPage();
        driver().get(ConfigReader.getBaseUrl() + "inventory.html");

        LoginPage loginPage = new LoginPage(driver());
        Assert.assertTrue(loginPage.getErrorMessage().contains("You can only access '/inventory.html' when you are logged in"),
                "Unexpected error message: " + loginPage.getErrorMessage());
        Assert.assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed.");
    }

    @Test(groups = { SMOKE, REGRESSION }, description = "A logged-in user can log out via the side menu")
    public void verifyUserCanLogOut() {
        LoginPage loginPage = loginAsStandardUser().logout();

        Assert.assertTrue(loginPage.isLoginFormDisplayed(), "Login form should be displayed after logout.");
        Assert.assertFalse(loginPage.getCurrentUrl().contains("inventory.html"),
                "User should have left the inventory page.");
    }

    @Test(groups = { REGRESSION }, description = "The password field masks the typed characters")
    public void verifyPasswordFieldIsMasked() {
        Assert.assertTrue(openLoginPage().isPasswordMasked(), "Password input should be of type 'password'.");
    }
}
