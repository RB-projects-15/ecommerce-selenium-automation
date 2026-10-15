package com.qa.automation.utils;

public final class XPaths {

    private XPaths() {
        // utility class
    }

    /** XPath predicate matching an element that has the given CSS class token. */
    public static String hasClass(String cssClass) {
        return "contains(concat(' ', normalize-space(@class), ' '), ' " + cssClass + " ')";
    }

    /** Quotes a value as an XPath string literal (handles single and double quotes). */
    public static String literal(String value) {
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        return "concat(\"" + value.replace("\"", "\", '\"', \"") + "\")";
    }
}
