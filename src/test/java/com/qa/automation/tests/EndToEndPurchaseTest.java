package com.qa.automation.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.automation.base.BaseTest;

public class EndToEndPurchaseTest extends BaseTest {

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By productButton =
            By.id("add-to-cart-sauce-labs-backpack");
    private final By cartIcon = By.className("shopping_cart_link");
    private final By checkoutButton = By.id("checkout");
    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By confirmationMessage =
            By.className("complete-header");

    private WebDriverWait getWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void verifyCompletePurchaseWorkflow() {
        WebDriverWait wait = getWait();

        // 1. Open the application and log in.
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

        // 2. Add a product to the cart.
        wait.until(
                ExpectedConditions.elementToBeClickable(productButton)
        ).click();

        // 3. Open the cart and verify the product.
        wait.until(
                ExpectedConditions.elementToBeClickable(cartIcon)
        ).click();

        Assert.assertTrue(
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.className("cart_item")
                        )
                ).isDisplayed(),
                "Cart item should be displayed"
        );

        // 4. Proceed to checkout.
        wait.until(
                ExpectedConditions.elementToBeClickable(checkoutButton)
        ).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(firstNameField)
        ).sendKeys("Rohan");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(lastNameField)
        ).sendKeys("QA");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(postalCodeField)
        ).sendKeys("403507");

        wait.until(
                ExpectedConditions.elementToBeClickable(continueButton)
        ).click();

        // 5. Verify the checkout overview.
        Assert.assertTrue(
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(
                                By.className("summary_info")
                        )
                ).isDisplayed(),
                "Checkout overview should be displayed"
        );

        // 6. Finish the purchase.
        wait.until(
                ExpectedConditions.elementToBeClickable(finishButton)
        ).click();

        // 7. Verify order confirmation.
        String actualMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        confirmationMessage
                )
        ).getText();

        Assert.assertEquals(
                actualMessage,
                "Thank you for your order!",
                "Order confirmation message is incorrect"
        );
    }
}
