package com.qa.automation.tests;

import static com.qa.automation.base.TestGroups.REGRESSION;
import static com.qa.automation.base.TestGroups.SMOKE;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.automation.base.BaseTest;
import com.qa.automation.data.Product;
import com.qa.automation.data.SortOption;
import com.qa.automation.data.TestData;
import com.qa.automation.pages.CheckoutCompletePage;
import com.qa.automation.pages.ProductsPage;

public class EndToEndPurchaseTests extends BaseTest {

    @Test(groups = { SMOKE, REGRESSION }, description = "A user can log in, buy several products and see the order confirmation")
    public void verifyCompletePurchaseWorkflow() {
        CheckoutCompletePage completePage = loginAsStandardUser()
                .addToCart(Product.BACKPACK)
                .addToCart(Product.BOLT_T_SHIRT)
                .openCart()
                .checkout()
                .fillForm(TestData.CHECKOUT_FIRST_NAME, TestData.CHECKOUT_LAST_NAME, TestData.CHECKOUT_POSTAL_CODE)
                .clickContinue()
                .finish();

        Assert.assertEquals(completePage.getConfirmationHeader(), TestData.ORDER_CONFIRMATION_HEADER,
                "Order confirmation message is incorrect.");
        Assert.assertEquals(completePage.getCartBadgeCount(), 0, "Cart should be empty after the order.");

        ProductsPage productsPage = completePage.backHome();
        Assert.assertEquals(productsPage.getTitle(), "Products", "Back Home should return to the Products page.");
    }

    @Test(groups = { REGRESSION }, description = "The most expensive product (found by sorting) can be purchased")
    public void verifyPurchaseOfMostExpensiveProductAfterSorting() {
        ProductsPage productsPage = loginAsStandardUser().sortBy(SortOption.PRICE_HIGH_LOW);

        String mostExpensive = productsPage.getProductNames().get(0);
        Assert.assertEquals(mostExpensive, Product.FLEECE_JACKET.title(), "Most expensive product should be listed first.");

        CheckoutCompletePage completePage = productsPage
                .addToCart(mostExpensive)
                .openCart()
                .checkout()
                .fillForm(TestData.CHECKOUT_FIRST_NAME, TestData.CHECKOUT_LAST_NAME, TestData.CHECKOUT_POSTAL_CODE)
                .clickContinue()
                .finish();

        Assert.assertEquals(completePage.getConfirmationHeader(), TestData.ORDER_CONFIRMATION_HEADER);
    }
}
