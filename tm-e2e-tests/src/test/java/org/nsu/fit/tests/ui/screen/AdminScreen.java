package org.nsu.fit.tests.ui.screen;

import java.util.List;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class AdminScreen extends Screen {
    public AdminScreen(Browser browser) {
        super(browser);
    }

    public CreateCustomerScreen createCustomer() {
        browser.click(By.xpath("//button[@title = 'Add Customer']"));
        return new CreateCustomerScreen(browser);
    }

    public CreatePlanScreen createPlan() {
        browser.click(By.xpath("//button[@title = 'Add plan']"));
        return new CreatePlanScreen(browser);
    }

    public List<String> getCustomers() {
        browser.waitForElement(By.className("MuiTableCell-root"));
        return browser.getValues(By.className("MuiTableCell-root"));
    }

    public List<String> getPlans() {
        browser.waitForElement(By.className("MuiTableCell-root"));
        return browser.getValues(By.className("MuiTableCell-root"));
    }

    public AdminScreen search(String search) {
        browser.typeText(By.xpath("//input[@aria-label = 'Search']"), search);
        return this;
    }

    public LoginScreen logout() {
        browser.waitForElement(By.linkText("Logout"));
        browser.click(By.linkText("Logout"));
        return new LoginScreen(browser);
    }
}
