package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.shared.Screen;
import org.openqa.selenium.By;

public class CreatePlanScreen extends Screen {
    public CreatePlanScreen(Browser browser) {
        super(browser);
    }

    public CreatePlanScreen fillName(String name) {
        browser.waitForElement(By.name("name"));
        browser.typeText(By.name("name"), name);
        return this;
    }

    public CreatePlanScreen fillDetails(String details) {
        browser.waitForElement(By.name("details"));
        browser.typeText(By.name("details"), details);
        return this;
    }

    public CreatePlanScreen fillFee(String fee) {
        browser.waitForElement(By.name("fee"));
        browser.typeText(By.name("fee"), fee);
        return this;
    }

    public AdminScreen clickSubmit() {
        browser.waitForElement(By.xpath("//button[@type = 'submit']"));
        browser.click(By.xpath("//button[@type = 'submit']"));
        return new AdminScreen(browser);
    }
}
