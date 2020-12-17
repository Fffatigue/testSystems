package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class TopUpBalanceScreen extends Screen {
    public TopUpBalanceScreen(Browser browser) {
        super(browser);
    }

    public CustomerScreen changeBalance(int balance) {
        String bala = Integer.toString(balance);
        browser.waitForElement(By.tagName("input"));
        browser.typeText(By.tagName("input"), bala);
        browser.waitForElement(By.xpath("//button[@type= 'submit']"));
        browser.click(By.xpath("//button[@type= 'submit']"));
        return new CustomerScreen(browser);
    }
}
