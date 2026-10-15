package com.qa.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.qa.automation.utils.PriceParser;

public class ProductDetailsPage extends BasePage {

    private static final By NAME = By.className("inventory_details_name");
    private static final By DESCRIPTION = By.className("inventory_details_desc");
    private static final By PRICE = By.className("inventory_details_price");
    private static final By ACTION_BUTTON = By.cssSelector("button.btn_inventory");
    private static final By BACK_BUTTON = By.id("back-to-products");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
        waitForUrlContaining("inventory-item.html");
        waitVisible(NAME);
    }

    public String getName() {
        return getText(NAME);
    }

    public String getDescription() {
        return getText(DESCRIPTION);
    }

    public double getPrice() {
        return PriceParser.parse(getText(PRICE));
    }

    public ProductDetailsPage addToCart() {
        click(ACTION_BUTTON);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(ACTION_BUTTON, "Remove"));
        return this;
    }

    public ProductsPage backToProducts() {
        click(BACK_BUTTON);
        return new ProductsPage(driver);
    }
}
