package org.nsu.fit.tests.ui;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginCustomerTest {
    private final String LOGIN = "test@test.com";
    private final String PASSWORD = "testtest";

    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Login customer via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Login customer feature")
    public void loginCustomer() {
        String customerName = new LoginScreen(browser).loginAsCustomer(LOGIN, PASSWORD).getCustomerName();
        Assert.assertEquals("Hi test test!", customerName);
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
