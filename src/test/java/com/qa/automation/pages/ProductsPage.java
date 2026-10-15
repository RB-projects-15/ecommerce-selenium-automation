package com.qa.automation.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.qa.automation.data.Product;
import com.qa.automation.data.SortOption;
import com.qa.automation.utils.PriceParser;
import com.qa.automation.utils.XPaths;

/** The inventory ("Products") page. */
public class ProductsPage extends BasePage {

    private static final By TITLE = By.className("title");
    private static final By ITEMS = By.className("inventory_item");
    private static final By ITEM_NAMES = By.className("inventory_item_name");
    private static final By ITEM_PRICES = By.className("inventory_item_price");
    private static final By SORT_DROPDOWN = By.className("product_sort_container");
    private static final By CART_LINK = By.className("shopping_cart_link");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT_LINK = By.id("logout_sidebar_link");
    private static final By RESET_LINK = By.id("reset_sidebar_link");

    public ProductsPage(WebDriver driver) {
        super(driver);
        waitForUrlContaining("inventory.html");
        waitVisible(TITLE);
    }

    public String getTitle() {
        return getText(TITLE);
    }

    public int getProductCount() {
        waitVisible(ITEMS);
        return driver.findElements(ITEMS).size();
    }

    public List<String> getProductNames() {
        waitVisible(ITEMS);
        return getTexts(ITEM_NAMES);
    }

    public List<Double> getProductPrices() {
        waitVisible(ITEMS);
        return getTexts(ITEM_PRICES).stream().map(PriceParser::parse).toList();
    }

    public boolean isSortDropdownDisplayed() {
        return waitVisible(SORT_DROPDOWN).isDisplayed();
    }

    public ProductsPage sortBy(SortOption option) {
        Select select = new Select(wait.until(ExpectedConditions.elementToBeClickable(SORT_DROPDOWN)));
        select.selectByValue(option.value());
        return this;
    }

    public ProductsPage addToCart(Product product) {
        return addToCart(product.title());
    }

    public ProductsPage addToCart(String productName) {
        By button = itemButton(productName);
        click(button);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(button, "Remove"));
        return this;
    }

    public ProductsPage removeFromCart(Product product) {
        By button = itemButton(product.title());
        click(button);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(button, "Add to cart"));
        return this;
    }

    public ProductDetailsPage openProductDetails(Product product) {
        click(itemName(product.title()));
        return new ProductDetailsPage(driver);
    }

    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }

    public LoginPage logout() {
        click(MENU_BUTTON);
        click(LOGOUT_LINK);
        return new LoginPage(driver).waitUntilLoaded();
    }

    /** Uses the side menu "Reset App State" action, which empties the cart. */
    public ProductsPage resetAppState() {
        click(MENU_BUTTON);
        click(RESET_LINK);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(CART_BADGE));
        return this;
    }

    private static By itemName(String productName) {
        return By.xpath("//div[" + XPaths.hasClass("inventory_item_name")
                + " and normalize-space()=" + XPaths.literal(productName) + "]");
    }

    private static By itemButton(String productName) {
        return By.xpath("//div[" + XPaths.hasClass("inventory_item_name")
                + " and normalize-space()=" + XPaths.literal(productName) + "]"
                + "/ancestor::div[" + XPaths.hasClass("inventory_item") + "]//button");
    }
}
