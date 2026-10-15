package com.qa.automation.tests;

import static com.qa.automation.base.TestGroups.REGRESSION;
import static com.qa.automation.base.TestGroups.SMOKE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.automation.base.BaseTest;
import com.qa.automation.data.Product;
import com.qa.automation.data.ProductDataProvider;
import com.qa.automation.data.SortOption;
import com.qa.automation.pages.ProductDetailsPage;
import com.qa.automation.pages.ProductsPage;

public class ProductsTests extends BaseTest {

    @Test(groups = { SMOKE,
            REGRESSION }, description = "Products page shows the title, all products and the sort dropdown")
    public void verifyProductsPageLayout() {
        ProductsPage productsPage = loginAsStandardUser();

        Assert.assertEquals(productsPage.getTitle(), "Products", "Products page title is incorrect.");
        Assert.assertEquals(productsPage.getProductCount(), Product.values().length, "Unexpected number of products.");
        Assert.assertTrue(productsPage.isSortDropdownDisplayed(), "Sort dropdown should be displayed.");
    }

    @Test(groups = { REGRESSION }, description = "Every catalogue product is listed with the correct price")
    public void verifyAllProductsAreListedWithCorrectPrices() {
        ProductsPage productsPage = loginAsStandardUser();

        List<String> names = productsPage.getProductNames();
        List<Double> prices = productsPage.getProductPrices();
        Assert.assertEquals(names.size(), prices.size(), "Every product should have a price.");

        for (Product product : Product.values()) {
            int index = names.indexOf(product.title());
            Assert.assertTrue(index >= 0, "Product not listed: " + product.title());
            double actualPrice = prices.get(index);
            Assert.assertEquals(actualPrice, product.price(), 0.001, "Wrong price for " + product.title());
        }
    }

    @Test(groups = {
            REGRESSION }, dataProvider = "sortOptions", dataProviderClass = ProductDataProvider.class, description = "Products can be sorted by name and price in both directions")
    public void verifyProductSorting(SortOption option) {
        ProductsPage productsPage = loginAsStandardUser().sortBy(option);

        switch (option) {
            case NAME_ASC -> {
                List<String> names = productsPage.getProductNames();
                Assert.assertEquals(names, sorted(names, false), "Names are not sorted A to Z.");
            }
            case NAME_DESC -> {
                List<String> names = productsPage.getProductNames();
                Assert.assertEquals(names, sorted(names, true), "Names are not sorted Z to A.");
            }
            case PRICE_LOW_HIGH -> {
                List<Double> prices = productsPage.getProductPrices();
                Assert.assertEquals(prices, sorted(prices, false), "Prices are not sorted low to high.");
            }
            case PRICE_HIGH_LOW -> {
                List<Double> prices = productsPage.getProductPrices();
                Assert.assertEquals(prices, sorted(prices, true), "Prices are not sorted high to low.");
            }
            default -> throw new IllegalStateException("Unhandled sort option: " + option);
        }
    }

    @Test(groups = {
            REGRESSION }, dataProvider = "allProducts", dataProviderClass = ProductDataProvider.class, description = "The product details page shows the correct name, price and description")
    public void verifyProductDetails(Product product) {
        ProductDetailsPage details = loginAsStandardUser().openProductDetails(product);

        Assert.assertEquals(details.getName(), product.title(), "Wrong product name on details page.");
        Assert.assertEquals(details.getPrice(), product.price(), 0.001, "Wrong product price on details page.");
        Assert.assertFalse(details.getDescription().isBlank(), "Product description should not be empty.");
    }

    @Test(groups = { REGRESSION }, description = "Back button on the details page returns to the Products page")
    public void verifyBackToProductsFromDetails() {
        ProductsPage productsPage = loginAsStandardUser()
                .openProductDetails(Product.BACKPACK)
                .backToProducts();

        Assert.assertEquals(productsPage.getProductCount(), Product.values().length);
    }

    private static <T extends Comparable<T>> List<T> sorted(List<T> values, boolean descending) {
        List<T> copy = new ArrayList<>(values);
        Collections.sort(copy);
        if (descending) {
            Collections.reverse(copy);
        }
        return copy;
    }
}
