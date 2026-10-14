
package com.qa.automation.tests;

import com.qa.automation.base.BaseTest;
import com.qa.automation.config.ConfigReader;
import com.qa.automation.data.LoginDataProvider;
import com.qa.automation.data.TestData;
import com.qa.automation.pages.LoginPage;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

        @Test(groups = { "smoke", "regression" })
        public void verifySuccessfulLogin() {

                driver.get(ConfigReader.getProperty("baseUrl"));

                LoginPage loginPage = new LoginPage(driver);
                loginPage.login(
                                TestData.VALID_USERNAME,
                                TestData.VALID_PASSWORD);

                Assert.assertTrue(
                                driver.getCurrentUrl().contains("inventory.html"),
                                "Products page was not displayed after login.");
        }

        @Test(dataProvider = "invalidLoginData", dataProviderClass = LoginDataProvider.class, groups = { "regression" })
        public void verifyInvalidLogin(
                        String username,
                        String password,
                        String expectedError) {

                driver.get(ConfigReader.getProperty("baseUrl"));

                LoginPage loginPage = new LoginPage(driver);
                loginPage.login(username, password);

                Assert.assertTrue(
                                loginPage.getErrorMessage().contains(expectedError),
                                "Expected error message was not displayed: "
                                                + expectedError);
        }
}
