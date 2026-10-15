package com.qa.automation.tests;

import static com.qa.automation.base.TestGroups.NEGATIVE;
import static com.qa.automation.base.TestGroups.REGRESSION;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.automation.base.BaseTest;
import com.qa.automation.data.CheckoutDataProvider;
import com.qa.automation.data.Product;
import com.qa.automation.data.TestData;
import com.qa.automation.pages.CheckoutInformationPage;
import com.qa.automation.pages.CheckoutOverviewPage;
import com.qa.automation.pages.ProductsPage;

public class CheckoutTests extends BaseTest {

    private CheckoutInformationPage openCheckoutWith(Product... products) {
        ProductsPage productsPage = loginAsStandardUser();
        for (Product product : products) {
            productsPage.addToCart(product);
        }
        return productsPage.openCart().checkout();
    }

    private CheckoutOverviewPage openOverviewWith(Product... products) {
        return openCheckoutWith(products)
                .fillForm(TestData.CHECKOUT_FIRST_NAME, TestData.CHECKOUT_LAST_NAME, TestData.CHECKOUT_POSTAL_CODE)
                .clickContinue();
    }

    @Test(groups = { REGRESSION, NEGATIVE },
            dataProvider = "invalidCheckoutDetails", dataProviderClass = CheckoutDataProvider.class,
            description = "Checkout validates the required customer information fields")
    public void verifyCheckoutFormValidation(String firstName, String lastName, String postalCode, String expectedError) {
        CheckoutInformationPage infoPage = openCheckoutWith(Product.BACKPACK)
                .fillForm(firstName, lastName, postalCode)
                .clickContinueExpectingError();

        Assert.assertTrue(infoPage.getErrorMessage().contains(expectedError),
                "Unexpected error message: " + infoPage.getErrorMessage());
        Assert.assertTrue(infoPage.getCurrentUrl().contains("checkout-step-one.html"),
                "User should remain on the customer information page.");
    }

    @Test(groups = { REGRESSION }, description = "Cancel on the information page returns to the cart with items intact")
    public void verifyCancelOnInformationPageReturnsToCart() {
        var cartPage = openCheckoutWith(Product.BACKPACK).cancel();

        Assert.assertEquals(cartPage.getItemNames(), List.of(Product.BACKPACK.title()));
    }

    @Test(groups = { REGRESSION }, description = "The order overview lists the selected products")
    public void verifyOverviewListsSelectedProducts() {
        CheckoutOverviewPage overview = openOverviewWith(Product.BACKPACK, Product.BIKE_LIGHT);

        Set<String> expected = Set.of(Product.BACKPACK.title(), Product.BIKE_LIGHT.title());
        Assert.assertEquals(new HashSet<>(overview.getItemNames()), expected, "Overview items are incorrect.");
    }

    @Test(groups = { REGRESSION }, description = "Item total, tax and grand total are calculated correctly")
    public void verifyOrderSummaryCalculations() {
        CheckoutOverviewPage overview = openOverviewWith(Product.BACKPACK, Product.BIKE_LIGHT, Product.ONESIE);

        double expectedItemTotal = Product.BACKPACK.price() + Product.BIKE_LIGHT.price() + Product.ONESIE.price();
        Assert.assertEquals(overview.getItemTotal(), expectedItemTotal, 0.01, "Item total is incorrect.");
        Assert.assertTrue(overview.getTax() > 0, "Tax should be greater than zero.");
        Assert.assertEquals(overview.getTotal(), overview.getItemTotal() + overview.getTax(), 0.01,
                "Grand total should equal item total plus tax.");
    }

    @Test(groups = { REGRESSION }, description = "Cancel on the overview page returns to the Products page")
    public void verifyCancelOnOverviewReturnsToProducts() {
        var productsPage = openOverviewWith(Product.BACKPACK).cancel();

        Assert.assertEquals(productsPage.getTitle(), "Products");
    }
}
