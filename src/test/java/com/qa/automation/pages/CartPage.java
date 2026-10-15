package com.qa.automation.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.qa.automation.data.Product;
import com.qa.automation.utils.PriceParser;
import com.qa.automation.utils.XPaths;

public class CartPage extends BasePage {

    private static final By TITLE = By.className("title");
    private static final By CART_ITEMS = By.className("cart_item");
    private static final By CART_ITEM_NAMES = By.cssSelector(".cart_item .inventory_item_name");
    private static final By CHECKOUT_BUTTON = By.id("checkout");
    private static final By CONTINUE_SHOPPING_BUTTON = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
        waitForUrlContaining("cart.html");
        wait.until(ExpectedConditions.textToBe(TITLE, "Your Cart"));
    }

    public int getItemCount() {
        return driver.findElements(CART_ITEMS).size();
    }

    public List<String> getItemNames() {
        return getTexts(CART_ITEM_NAMES);
    }

    public double getItemPrice(Product product) {
        return PriceParser.parse(getText(By.xpath(cartItem(product) + "//div[" + XPaths.hasClass("inventory_item_price") + "]")));
    }

    public int getItemQuantity(Product product) {
        return Integer.parseInt(getText(By.xpath(cartItem(product) + "//div[" + XPaths.hasClass("cart_quantity") + "]")));
    }

    public CartPage removeItem(Product product) {
        click(By.xpath(cartItem(product) + "//button"));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(cartItem(product))));
        return this;
    }

    public ProductsPage continueShopping() {
        click(CONTINUE_SHOPPING_BUTTON);
        return new ProductsPage(driver);
    }

    public CheckoutInformationPage checkout() {
        click(CHECKOUT_BUTTON);
        return new CheckoutInformationPage(driver);
    }

    private static String cartItem(Product product) {
        return "//div[" + XPaths.hasClass("cart_item") + "][.//div[" + XPaths.hasClass("inventory_item_name")
                + " and normalize-space()=" + XPaths.literal(product.title()) + "]]";
    }
}
