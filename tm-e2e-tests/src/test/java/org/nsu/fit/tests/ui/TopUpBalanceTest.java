package org.nsu.fit.tests.ui;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.nsu.fit.tests.ui.screen.CustomerScreen;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TopUpBalanceTest {
    private final String LOGIN = "test@test.com";
    private final String PASSWORD = "testtest";
    private final int BALANCE_ADD = 500;


    private Browser browser = null;
    private CustomerScreen customerScreen;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
        customerScreen = new LoginScreen(browser).loginAsCustomer(LOGIN, PASSWORD);
    }

    @Test(description = "Top up balance via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Top up balance feature")
    public void topUpBalance() {
        int prevBalance = customerScreen.getBalance();
        customerScreen = customerScreen.goToBalance().changeBalance(BALANCE_ADD);
        int curBalance = customerScreen.getBalance();
        Assert.assertEquals(prevBalance + BALANCE_ADD, curBalance);
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
