
package com.qa.automation.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.automation.base.BaseTest;

public class CartContentTest extends BaseTest {

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By addToCartButton =
            By.id("add-to-cart-sauce-labs-backpack");
    private final By cartIcon = By.className("shopping_cart_link");
    private final By cartItem = By.className("cart_item");
    private final By itemName =
            By.className("inventory_item_name");
    private final By itemPrice =
            By.className("inventory_item_price");
    private final By itemQuantity =
            By.className("cart_quantity");

    private WebDriverWait getWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void verifyCartProductDetails() {
        WebDriverWait wait = getWait();

        driver.get("https://www.saucedemo.com/");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(usernameField)
        ).sendKeys("standard_user");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(passwordField)
        ).sendKeys("secret_sauce");

        wait.until(
                ExpectedConditions.elementToBeClickable(loginButton)
        ).click();

        wait.until(
                ExpectedConditions.elementToBeClickable(addToCartButton)
        ).click();

        wait.until(
                ExpectedConditions.elementToBeClickable(cartIcon)
        ).click();

        WebElement cartItemElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(cartItem)
        );

        String actualName = cartItemElement.findElement(itemName).getText();
        String actualPrice = cartItemElement.findElement(itemPrice).getText();
        String actualQuantity =
                cartItemElement.findElement(itemQuantity).getText();

        Assert.assertEquals(
                actualName,
                "Sauce Labs Backpack",
                "Incorrect product name in cart"
        );

        Assert.assertEquals(
                actualPrice,
                "$29.99",
                "Incorrect product price in cart"
        );

        Assert.assertEquals(
                actualQuantity,
                "1",
                "Incorrect product quantity in cart"
        );
    }
}
