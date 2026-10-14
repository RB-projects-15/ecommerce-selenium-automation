package com.qa.automation.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.automation.utils.WaitUtils;

public class CartPage {

        private final WebDriver driver;
        private final WaitUtils wait;

        private final By cartLink = By.className("shopping_cart_link");
        private final By cartItems = By.className("cart_item");
        private final By cartItemName = By.className("inventory_item_name");
        private final By removeButton = By.id("remove-sauce-labs-backpack");

        public CartPage(WebDriver driver) {
                this.driver = driver;
                this.wait = new WaitUtils(driver);
        }

        public void openCart() {
                wait.waitForElementClickable(cartLink).click();
        }

        public int getCartItemCount() {
                return driver.findElements(cartItems).size();
        }

        public String getCartItemName() {
                return wait.waitForElementVisible(cartItemName).getText();
        }

        public void removeBackpackFromCart() {
                wait.waitForElementClickable(removeButton).click();

                // Wait until no cart items remain
                new WebDriverWait(driver, Duration.ofSeconds(10))
                                .until(ExpectedConditions.numberOfElementsToBe(cartItems, 0));
        }
}