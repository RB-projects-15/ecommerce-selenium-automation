package com.qa.automation.tests;

import com.qa.automation.base.BaseTest;
import com.qa.automation.config.ConfigReader;
import com.qa.automation.data.TestData;
import com.qa.automation.pages.CartPage;
import com.qa.automation.pages.LoginPage;
import com.qa.automation.pages.ProductsPage;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    @Test(groups = { "smoke", "regression" })
    public void verifyProductCanBeAddedToCart() {

        // Open the application
        driver.get(ConfigReader.getProperty("baseUrl"));

        // Log in
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(
                TestData.VALID_USERNAME,
                TestData.VALID_PASSWORD);

        // Add a product to the cart
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.addBackpackToCart();

        // Open the cart
        CartPage cartPage = new CartPage(driver);
        cartPage.openCart();

        // Verify that exactly one product is in the cart
        Assert.assertEquals(
                cartPage.getCartItemCount(),
                1,
                "Cart should contain exactly one product.");

        // Verify the product name
        Assert.assertEquals(
                cartPage.getCartItemName(),
                "Sauce Labs Backpack",
                "The expected product was not found in the cart.");
    }
}
