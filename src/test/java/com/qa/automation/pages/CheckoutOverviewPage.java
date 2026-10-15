package com.qa.automation.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.automation.utils.PriceParser;

/** Checkout step two: order summary. */
public class CheckoutOverviewPage extends BasePage {

    private static final By ITEM_NAMES = By.cssSelector(".cart_item .inventory_item_name");
    private static final By ITEM_TOTAL = By.className("summary_subtotal_label");
    private static final By TAX = By.className("summary_tax_label");
    private static final By TOTAL = By.className("summary_total_label");
    private static final By FINISH_BUTTON = By.id("finish");
    private static final By CANCEL_BUTTON = By.id("cancel");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
        waitForUrlContaining("checkout-step-two.html");
        waitVisible(TOTAL);
    }

    public List<String> getItemNames() {
        return getTexts(ITEM_NAMES);
    }

    public double getItemTotal() {
        return PriceParser.parse(getText(ITEM_TOTAL));
    }

    public double getTax() {
        return PriceParser.parse(getText(TAX));
    }

    public double getTotal() {
        return PriceParser.parse(getText(TOTAL));
    }

    public CheckoutCompletePage finish() {
        click(FINISH_BUTTON);
        return new CheckoutCompletePage(driver);
    }

    public ProductsPage cancel() {
        click(CANCEL_BUTTON);
        return new ProductsPage(driver);
    }
}
