package com.qa.automation.data;

import org.testng.annotations.DataProvider;

public class LoginDataProvider {

    private static final String MISMATCH = "Username and password do not match";

    /** Columns: username, password, expected error fragment. */
    @DataProvider(name = "invalidCredentials", parallel = true)
    public static Object[][] invalidCredentials() {
        return new Object[][] {
                { "standard_user", "wrong_password", MISMATCH },
                { "invalid_user", TestData.PASSWORD, MISMATCH },
                { "' OR '1'='1", "' OR '1'='1", MISMATCH },
                { "", TestData.PASSWORD, "Username is required" },
                { "standard_user", "", "Password is required" },
                { "", "", "Username is required" }
        };
    }
}
