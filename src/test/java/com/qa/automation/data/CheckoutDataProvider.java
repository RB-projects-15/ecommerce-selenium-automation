package com.qa.automation.data;

import org.testng.annotations.DataProvider;

public class CheckoutDataProvider {

    /** Columns: first name, last name, postal code, expected error fragment. */
    @DataProvider(name = "invalidCheckoutDetails", parallel = true)
    public static Object[][] invalidCheckoutDetails() {
        return new Object[][] {
                { "", TestData.CHECKOUT_LAST_NAME, TestData.CHECKOUT_POSTAL_CODE, "First Name is required" },
                { TestData.CHECKOUT_FIRST_NAME, "", TestData.CHECKOUT_POSTAL_CODE, "Last Name is required" },
                { TestData.CHECKOUT_FIRST_NAME, TestData.CHECKOUT_LAST_NAME, "", "Postal Code is required" },
                { "", "", "", "First Name is required" }
        };
    }
}
