
package com.qa.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.automation.utils.WaitUtils;

public class CheckoutPage {

    private final WaitUtils wait;

    // Checkout page locators
    private final By checkoutButton = By.id("checkout");
    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By confirmationMessage =
            By.className("complete-header");

    // Constructor
    public CheckoutPage(WebDriver driver) {
        this.wait = new WaitUtils(driver);
    }

    // Click the Checkout button
    public void clickCheckout() {
        wait.waitForElementClickable(checkoutButton).click();
    }

    // Enter customer details
    public void enterCustomerDetails(
            String firstName,
            String lastName,
            String postalCode) {

        wait.waitForElementVisible(firstNameField)
                .sendKeys(firstName);

        wait.waitForElementVisible(lastNameField)
                .sendKeys(lastName);

        wait.waitForElementVisible(postalCodeField)
                .sendKeys(postalCode);
    }

    // Click Continue
    public void clickContinue() {
        wait.waitForElementClickable(continueButton).click();
    }

    // Finish the order
    public void clickFinish() {
        wait.waitForElementClickable(finishButton).click();
    }

    // Get the order confirmation message
    public String getConfirmationMessage() {
        return wait.waitForElementVisible(confirmationMessage)
                .getText();
    }
}
