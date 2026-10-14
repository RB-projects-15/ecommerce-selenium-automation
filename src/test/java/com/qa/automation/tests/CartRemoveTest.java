package com.qa.automation.tests;

import com.qa.automation.base.BaseTest;
import com.qa.automation.config.ConfigReader;
import com.qa.automation.data.TestData;
import com.qa.automation.pages.CartPage;
import com.qa.automation.pages.LoginPage;
import com.qa.automation.pages.ProductsPage;

import org.testng.Assert;
import org.testng.annotations.Test;

public class CartRemoveTest extends BaseTest {

    @Test(groups = { "regression" })
    public void verifyProductCanBeRemovedFromCart() {

        driver.get(ConfigReader.getProperty("baseUrl"));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(
                TestData.VALID_USERNAME,
                TestData.VALID_PASSWORD);

        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.addBackpackToCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.openCart();

        // Waits for the item to be visible, so the cart page is fully loaded
        Assert.assertEquals(
                cartPage.getCartItemName(),
                "Sauce Labs Backpack",
                "Cart should contain the backpack before removal.");

        Assert.assertEquals(
                cartPage.getCartItemCount(),
                1,
                "Cart should contain exactly one product before removal.");

        cartPage.removeBackpackFromCart();

        Assert.assertEquals(
                cartPage.getCartItemCount(),
                0,
                "Cart should be empty after removing the backpack.");
    }
}