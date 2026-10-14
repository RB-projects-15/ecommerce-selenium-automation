
package com.qa.automation.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.qa.automation.utils.WaitUtils;

public class ProductsPage {

    private final WebDriver driver;
    private final WaitUtils wait;

    private final By pageTitle = By.className("title");
    private final By inventoryItems = By.className("inventory_item");
    private final By sortDropdown = By.className("product_sort_container");
    private final By productPrices = By.className("inventory_item_price");
    private final By addToCartButton = By.id("add-to-cart-sauce-labs-backpack");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    public String getPageTitle() {
        return wait.waitForElementVisible(pageTitle).getText();
    }

    public int getProductCount() {
        wait.waitForElementVisible(inventoryItems);
        return driver.findElements(inventoryItems).size();
    }

    public boolean isSortDropdownDisplayed() {
        return wait.waitForElementVisible(sortDropdown).isDisplayed();
    }

    public void sortByPriceLowToHigh() {
        WebElement dropdown = wait.waitForElementClickable(sortDropdown);
        Select select = new Select(dropdown);
        select.selectByValue("lohi");
    }

    public List<Double> getProductPrices() {
        List<WebElement> elements = driver.findElements(productPrices);
        List<Double> prices = new ArrayList<>();

        for (WebElement element : elements) {
            String priceText = element.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(priceText));
        }

        return prices;
    }

    public void addBackpackToCart() {
        wait.waitForElementClickable(addToCartButton).click();
    }
}
