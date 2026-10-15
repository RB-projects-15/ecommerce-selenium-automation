package com.qa.automation.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PriceParser {

    private static final Pattern PRICE = Pattern.compile("\\$(\\d+(?:\\.\\d+)?)");

    private PriceParser() {
        // utility class
    }

    /** Extracts the first dollar amount from text such as "$29.99" or "Item total: $29.99". */
    public static double parse(String text) {
        Matcher matcher = PRICE.matcher(text);
        if (!matcher.find()) {
            throw new IllegalArgumentException("No price found in text: '" + text + "'");
        }
        return Double.parseDouble(matcher.group(1));
    }
}
