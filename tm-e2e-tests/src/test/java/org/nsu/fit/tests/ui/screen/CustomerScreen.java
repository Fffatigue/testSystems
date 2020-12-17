package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class CustomerScreen extends Screen {
    public CustomerScreen(Browser browser) {
        super(browser);
    }

    public String getCustomerName() {
        return browser.getText(By.xpath("//div[@class = 'col-md-32 col-md-offset-0']/h2"));
    }

    public int getBalance() {
        browser.waitForElement(By.linkText("Top up balance"));
        String balanceMsg = browser.getText(By.xpath("//div/h3"));
        return Integer.parseInt(balanceMsg.replace("Your balance: ", ""));
    }

    public TopUpBalanceScreen goToBalance() {
        browser.waitForElement(By.linkText("Top up balance"));
        browser.click(By.linkText("Top up balance"));
        return new TopUpBalanceScreen(browser);
    }

}
