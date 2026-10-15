package com.qa.automation.base;

/** TestNG group names used to select which tests run (see testng-smoke.xml). */
public final class TestGroups {

    public static final String SMOKE = "smoke";
    public static final String REGRESSION = "regression";
    public static final String NEGATIVE = "negative";

    private TestGroups() {
        // constants only
    }
}
