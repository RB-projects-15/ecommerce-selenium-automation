package com.qa.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Order confirmation page. */
public class CheckoutCompletePage extends BasePage {

    private static final By HEADER = By.className("complete-header");
    private static final By BACK_HOME_BUTTON = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
        waitForUrlContaining("checkout-complete.html");
        waitVisible(HEADER);
    }

    public String getConfirmationHeader() {
        return getText(HEADER);
    }

    public ProductsPage backHome() {
        click(BACK_HOME_BUTTON);
        return new ProductsPage(driver);
    }
}
