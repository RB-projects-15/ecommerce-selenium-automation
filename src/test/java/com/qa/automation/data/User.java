package com.qa.automation.data;

/** Demo users provided by SauceDemo. All share the same password. */
public enum User {

    STANDARD("standard_user"),
    LOCKED_OUT("locked_out_user");

    private final String username;

    User(String username) {
        this.username = username;
    }

    public String username() {
        return username;
    }

    public String password() {
        return TestData.PASSWORD;
    }
}
