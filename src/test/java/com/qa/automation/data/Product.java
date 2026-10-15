package com.qa.automation.data;

/** The SauceDemo product catalogue (expected name and price for each item). */
public enum Product {

    BACKPACK("Sauce Labs Backpack", 29.99),
    BIKE_LIGHT("Sauce Labs Bike Light", 9.99),
    BOLT_T_SHIRT("Sauce Labs Bolt T-Shirt", 15.99),
    FLEECE_JACKET("Sauce Labs Fleece Jacket", 49.99),
    ONESIE("Sauce Labs Onesie", 7.99),
    RED_T_SHIRT("Test.allTheThings() T-Shirt (Red)", 15.99);

    private final String title;
    private final double price;

    Product(String title, double price) {
        this.title = title;
        this.price = price;
    }

    public String title() {
        return title;
    }

    public double price() {
        return price;
    }

    @Override
    public String toString() {
        return title;
    }
}
