package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class CreateCustomerScreen extends Screen {
    public CreateCustomerScreen(Browser browser) {
        super(browser);
    }

    public CreateCustomerScreen fillEmail(String email) {
        browser.waitForElement(By.name("login"));
        browser.typeText(By.name("login"), email);
        return this;
    }

    public CreateCustomerScreen fillPassword(String password) {
        browser.waitForElement(By.name("//*[@name = '']"));
        browser.typeText(By.name("password"), password);
        return this;
    }

    public CreateCustomerScreen fillFirstName(String firstName) {
        browser.waitForElement(By.name("firstName"));
        browser.typeText(By.name("firstName"), firstName);
        return this;
    }

    public CreateCustomerScreen fillLastName(String lastName) {
        browser.waitForElement(By.name("lastName"));
        browser.typeText(By.name("lastName"), lastName);
        return this;
    }

    public AdminScreen clickSubmit() {
        browser.waitForElement(By.xpath("//button[@type = 'submit']"));
        browser.click(By.xpath("//button[@type = 'submit']"));
        if (browser.isElementPresent(By.xpath("//div[@role = 'alert']"))) {
            throw new IllegalArgumentException("Can't create customer");
        }
        return new AdminScreen(browser);
    }

    public AdminScreen clickCancel() {
        browser.waitForElement(By.xpath("//button[@type = 'button']"));
        browser.click(By.xpath("//button[@type = 'button']"));
        return new AdminScreen(browser);
    }
}
