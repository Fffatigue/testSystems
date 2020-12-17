package org.nsu.fit.tests.ui;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LogoutTest {
    private final String LOGIN = "test@test.com";
    private final String PASSWORD = "testtest";

    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Logout via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Logout feature")
    public void logout() {
        new LoginScreen(browser).loginAsAdmin().logout().loginAsCustomer(LOGIN, PASSWORD);
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
