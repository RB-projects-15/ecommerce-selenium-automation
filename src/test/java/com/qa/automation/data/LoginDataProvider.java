package com.qa.automation.data;

import org.testng.annotations.DataProvider;

public class LoginDataProvider {

        @DataProvider(name = "invalidLoginData")
        public static Object[][] invalidLoginData() {

                return new Object[][] {
                                { "standard_user", "wrong_password",
                                                "Username and password do not match" },
                                { "", "secret_sauce",
                                                "Username is required" },
                                { "standard_user", "",
                                                "Password is required" }
                };
        }
}
