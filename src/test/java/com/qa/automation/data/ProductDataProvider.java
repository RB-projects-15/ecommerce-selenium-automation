package com.qa.automation.data;

import org.testng.annotations.DataProvider;

public class ProductDataProvider {

    @DataProvider(name = "allProducts", parallel = true)
    public static Object[][] allProducts() {
        Product[] products = Product.values();
        Object[][] data = new Object[products.length][1];
        for (int i = 0; i < products.length; i++) {
            data[i][0] = products[i];
        }
        return data;
    }

    @DataProvider(name = "sortOptions", parallel = true)
    public static Object[][] sortOptions() {
        SortOption[] options = SortOption.values();
        Object[][] data = new Object[options.length][1];
        for (int i = 0; i < options.length; i++) {
            data[i][0] = options[i];
        }
        return data;
    }
}
