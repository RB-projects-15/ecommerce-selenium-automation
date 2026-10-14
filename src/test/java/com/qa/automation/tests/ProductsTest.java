
package com.qa.automation.tests;

import com.qa.automation.base.BaseTest;
import com.qa.automation.config.ConfigReader;
import com.qa.automation.data.TestData;
import com.qa.automation.pages.LoginPage;
import com.qa.automation.pages.ProductsPage;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ProductsTest extends BaseTest {

    @Test(groups = { "smoke", "regression" })
    public void verifyProductsPageLoads() {

        driver.get(ConfigReader.getProperty("baseUrl"));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(
                TestData.VALID_USERNAME,
                TestData.VALID_PASSWORD);

        ProductsPage productsPage = new ProductsPage(driver);

        Assert.assertEquals(
                productsPage.getPageTitle(),
                "Products",
                "Products page title is incorrect.");

        Assert.assertTrue(
                productsPage.getProductCount() > 0,
                "No products are displayed.");

        Assert.assertTrue(
                productsPage.isSortDropdownDisplayed(),
                "Product sorting dropdown is not displayed.");
    }
}
