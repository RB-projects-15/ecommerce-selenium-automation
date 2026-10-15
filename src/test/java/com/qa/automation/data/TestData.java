package com.qa.automation.data;

/** Shared constants for test data. SauceDemo credentials are public demo values. */
public final class TestData {

    public static final String PASSWORD = "secret_sauce";

    public static final String CHECKOUT_FIRST_NAME = "John";
    public static final String CHECKOUT_LAST_NAME = "Doe";
    public static final String CHECKOUT_POSTAL_CODE = "10001";

    public static final String ORDER_CONFIRMATION_HEADER = "Thank you for your order!";

    private TestData() {
        // constants only
    }
}
