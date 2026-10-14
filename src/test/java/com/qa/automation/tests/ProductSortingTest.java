
package com.qa.automation.tests;

import java.util.List;

import com.qa.automation.base.BaseTest;
import com.qa.automation.config.ConfigReader;
import com.qa.automation.data.TestData;
import com.qa.automation.pages.LoginPage;
import com.qa.automation.pages.ProductsPage;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductSortingTest extends BaseTest {

    @Test(groups = { "regression" })
    public void verifyProductsSortByPriceLowToHigh() {

        driver.get(ConfigReader.getProperty("baseUrl"));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(
                TestData.VALID_USERNAME,
                TestData.VALID_PASSWORD);

        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.sortByPriceLowToHigh();

        List<Double> prices = productsPage.getProductPrices();

        Assert.assertFalse(
                prices.isEmpty(),
                "No product prices were found.");

        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(
                    prices.get(i) <= prices.get(i + 1),
                    "Products are not sorted from low to high: " + prices);
        }
    }
}
