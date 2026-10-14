package com.qa.automation.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.automation.base.BaseTest;

public class CheckoutValidationTest extends BaseTest {

        private final By errorMessage = By.cssSelector("[data-test='error']");

        private final By usernameField = By.id("user-name");
        private final By passwordField = By.id("password");
        private final By loginButton = By.id("login-button");
        private final By productsTitle = By.className("title");

        private final By addToCartButton = By.id("add-to-cart-sauce-labs-backpack");
        private final By cartIcon = By.className("shopping_cart_link");
        private final By cartItem = By.className("cart_item");
        private final By checkoutButton = By.id("checkout");

        private final By firstNameField = By.id("first-name");
        private final By lastNameField = By.id("last-name");
        private final By postalCodeField = By.id("postal-code");
        private final By continueButton = By.id("continue");

        private WebDriverWait getWait() {
                return new WebDriverWait(driver, Duration.ofSeconds(15));
        }

        private void openCheckout() {
                WebDriverWait wait = getWait();

                // Open the login page.
                driver.get("https://www.saucedemo.com/");

                wait.until(
                                ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys("standard_user");

                wait.until(
                                ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys("secret_sauce");

                wait.until(
                                ExpectedConditions.elementToBeClickable(loginButton)).click();

                // Confirm successful navigation to the products page.
                wait.until(
                                ExpectedConditions.urlContains("inventory.html"));

                wait.until(
                                ExpectedConditions.visibilityOfElementLocated(productsTitle));

                // Add the backpack to the cart.
                wait.until(
                                ExpectedConditions.elementToBeClickable(addToCartButton)).click();

                // Open the cart and confirm the product is present.
                wait.until(
                                ExpectedConditions.elementToBeClickable(cartIcon)).click();

                wait.until(
                                ExpectedConditions.urlContains("cart.html"));

                wait.until(
                                ExpectedConditions.visibilityOfElementLocated(cartItem));

                // Click checkout.
                wait.until(
                                ExpectedConditions.elementToBeClickable(checkoutButton)).click();

                // Confirm the checkout information page has loaded.
                wait.until(
                                ExpectedConditions.urlContains("checkout-step-one.html"));

                wait.until(
                                ExpectedConditions.visibilityOfElementLocated(firstNameField));

                Assert.assertTrue(
                                driver.getCurrentUrl().contains("checkout-step-one.html"),
                                "Checkout information page did not open. Current URL: "
                                                + driver.getCurrentUrl());
        }

        private void verifyError(String expectedError) {
                WebDriverWait wait = getWait();

                String actualError = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();

                Assert.assertTrue(
                                actualError.contains(expectedError),
                                "Expected error: " + expectedError
                                                + ", but got: " + actualError);

                Assert.assertTrue(
                                driver.getCurrentUrl().contains("checkout-step-one.html"),
                                "Checkout should remain on the customer information page.");
        }

        private WebElement waitForField(By locator) {
                return getWait().until(
                                ExpectedConditions.visibilityOfElementLocated(locator));
        }

        @Test
        public void verifyMissingFirstName() {
                openCheckout();

                waitForField(lastNameField).sendKeys("QA");
                waitForField(postalCodeField).sendKeys("403507");

                getWait().until(
                                ExpectedConditions.elementToBeClickable(continueButton)).click();

                verifyError("First Name is required");
        }

        @Test
        public void verifyMissingLastName() {
                openCheckout();

                waitForField(firstNameField).sendKeys("Rohan");
                waitForField(postalCodeField).sendKeys("403507");

                getWait().until(
                                ExpectedConditions.elementToBeClickable(continueButton)).click();

                verifyError("Last Name is required");
        }

        @Test
        public void verifyMissingPostalCode() {
                openCheckout();

                waitForField(firstNameField).sendKeys("Rohan");
                waitForField(lastNameField).sendKeys("QA");

                getWait().until(
                                ExpectedConditions.elementToBeClickable(continueButton)).click();

                verifyError("Postal Code is required");
        }
}
