package com.qa.automation.tests;

import static com.qa.automation.base.TestGroups.REGRESSION;
import static com.qa.automation.base.TestGroups.SMOKE;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.automation.base.BaseTest;
import com.qa.automation.data.Product;
import com.qa.automation.pages.CartPage;
import com.qa.automation.pages.ProductsPage;

public class CartTests extends BaseTest {

    @Test(groups = { REGRESSION }, description = "A new session starts with an empty cart")
    public void verifyCartIsEmptyOnFreshLogin() {
        ProductsPage productsPage = loginAsStandardUser();
        Assert.assertEquals(productsPage.getCartBadgeCount(), 0, "Cart badge should not be shown.");

        CartPage cartPage = productsPage.openCart();
        Assert.assertEquals(cartPage.getItemCount(), 0, "Cart should be empty.");
    }

    @Test(groups = { SMOKE, REGRESSION }, description = "A product can be added and appears in the cart with correct details")
    public void verifyProductCanBeAddedToCart() {
        ProductsPage productsPage = loginAsStandardUser().addToCart(Product.BACKPACK);
        Assert.assertEquals(productsPage.getCartBadgeCount(), 1, "Cart badge should show 1 item.");

        CartPage cartPage = productsPage.openCart();
        Assert.assertEquals(cartPage.getItemNames(), List.of(Product.BACKPACK.title()),
                "Cart should contain only the backpack.");
        Assert.assertEquals(cartPage.getItemPrice(Product.BACKPACK), Product.BACKPACK.price(), 0.001);
        Assert.assertEquals(cartPage.getItemQuantity(Product.BACKPACK), 1);
    }

    @Test(groups = { REGRESSION }, description = "Several products can be added to the cart")
    public void verifyMultipleProductsCanBeAdded() {
        ProductsPage productsPage = loginAsStandardUser()
                .addToCart(Product.BACKPACK)
                .addToCart(Product.BIKE_LIGHT)
                .addToCart(Product.ONESIE);
        Assert.assertEquals(productsPage.getCartBadgeCount(), 3, "Cart badge should show 3 items.");

        CartPage cartPage = productsPage.openCart();
        Set<String> expected = Set.of(Product.BACKPACK.title(), Product.BIKE_LIGHT.title(), Product.ONESIE.title());
        Assert.assertEquals(cartPage.getItemCount(), 3);
        Assert.assertEquals(new HashSet<>(cartPage.getItemNames()), expected, "Cart contents are incorrect.");
    }

    @Test(groups = { SMOKE, REGRESSION }, description = "A product can be removed from the cart page")
    public void verifyProductCanBeRemovedFromCartPage() {
        CartPage cartPage = loginAsStandardUser()
                .addToCart(Product.BACKPACK)
                .openCart()
                .removeItem(Product.BACKPACK);

        Assert.assertEquals(cartPage.getItemCount(), 0, "Cart should be empty after removal.");
        Assert.assertEquals(cartPage.getCartBadgeCount(), 0, "Cart badge should disappear.");
    }

    @Test(groups = { REGRESSION }, description = "A product can be removed from the Products page")
    public void verifyProductCanBeRemovedFromProductsPage() {
        ProductsPage productsPage = loginAsStandardUser()
                .addToCart(Product.BOLT_T_SHIRT)
                .removeFromCart(Product.BOLT_T_SHIRT);

        Assert.assertEquals(productsPage.getCartBadgeCount(), 0, "Cart badge should disappear.");
    }

    @Test(groups = { REGRESSION }, description = "Continue Shopping returns to the Products page")
    public void verifyContinueShoppingReturnsToProducts() {
        ProductsPage productsPage = loginAsStandardUser()
                .openCart()
                .continueShopping();

        Assert.assertEquals(productsPage.getTitle(), "Products");
    }

    @Test(groups = { REGRESSION }, description = "The cart keeps its items while navigating between pages")
    public void verifyCartPersistsWhileNavigating() {
        ProductsPage productsPage = loginAsStandardUser()
                .addToCart(Product.BACKPACK)
                .openProductDetails(Product.FLEECE_JACKET)
                .backToProducts();

        Assert.assertEquals(productsPage.getCartBadgeCount(), 1, "Cart should still contain the backpack.");
    }

    @Test(groups = { REGRESSION }, description = "A product can be added from its details page")
    public void verifyProductCanBeAddedFromDetailsPage() {
        ProductsPage productsPage = loginAsStandardUser()
                .openProductDetails(Product.RED_T_SHIRT)
                .addToCart()
                .backToProducts();

        Assert.assertEquals(productsPage.getCartBadgeCount(), 1, "Cart badge should show 1 item.");

        CartPage cartPage = productsPage.openCart();
        Assert.assertEquals(cartPage.getItemNames(), List.of(Product.RED_T_SHIRT.title()));
    }

    @Test(groups = { REGRESSION }, description = "Reset App State empties the cart")
    public void verifyResetAppStateClearsCart() {
        ProductsPage productsPage = loginAsStandardUser()
                .addToCart(Product.BACKPACK)
                .addToCart(Product.ONESIE)
                .resetAppState();

        Assert.assertEquals(productsPage.getCartBadgeCount(), 0, "Cart badge should be cleared.");
        Assert.assertEquals(productsPage.openCart().getItemCount(), 0, "Cart should be empty after reset.");
    }
}
