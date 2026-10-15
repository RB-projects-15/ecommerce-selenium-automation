package com.qa.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Checkout step one: customer information form. */
public class CheckoutInformationPage extends BasePage {

    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE_BUTTON = By.id("continue");
    private static final By CANCEL_BUTTON = By.id("cancel");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    public CheckoutInformationPage(WebDriver driver) {
        super(driver);
        waitForUrlContaining("checkout-step-one.html");
        waitVisible(FIRST_NAME);
    }

    public CheckoutInformationPage fillForm(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME, firstName);
        type(LAST_NAME, lastName);
        type(POSTAL_CODE, postalCode);
        return this;
    }

    /** Continues with valid details; lands on the order overview. */
    public CheckoutOverviewPage clickContinue() {
        click(CONTINUE_BUTTON);
        return new CheckoutOverviewPage(driver);
    }

    /** Continues with invalid details; stays on this page and shows a validation error. */
    public CheckoutInformationPage clickContinueExpectingError() {
        click(CONTINUE_BUTTON);
        waitVisible(ERROR_MESSAGE);
        return this;
    }

    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    public CartPage cancel() {
        click(CANCEL_BUTTON);
        return new CartPage(driver);
    }
}
