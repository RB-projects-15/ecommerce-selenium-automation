package com.qa.automation.data;

/** Options of the product sort dropdown (value attribute and visible label). */
public enum SortOption {

    NAME_ASC("az", "Name (A to Z)"),
    NAME_DESC("za", "Name (Z to A)"),
    PRICE_LOW_HIGH("lohi", "Price (low to high)"),
    PRICE_HIGH_LOW("hilo", "Price (high to low)");

    private final String value;
    private final String label;

    SortOption(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String value() {
        return value;
    }

    public String label() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
