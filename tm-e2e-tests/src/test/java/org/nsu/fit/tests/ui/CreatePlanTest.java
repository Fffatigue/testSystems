package org.nsu.fit.tests.ui;

import java.util.List;

import com.github.javafaker.Faker;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.nsu.fit.tests.ui.screen.AdminScreen;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CreatePlanTest {
    private Browser browser = null;
    private AdminScreen adminScreen = null;

    private String name = null;
    private String details = null;
    private String fee = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();

        Faker faker = new Faker();
        name = faker.name().username() + faker.number().numberBetween(0, 5000);
        details = faker.animal().name() + faker.number().numberBetween(0, 5000);
        fee = Integer.toString(faker.number().numberBetween(0, 5000));
    }

    @Test(description = "Create plan via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create plan feature")
    public void createPlan() {
        adminScreen = new LoginScreen(browser)
                .loginAsAdmin()
                .createPlan()
                .fillDetails(details)
                .fillName(name)
                .fillFee(fee)
                .clickSubmit();
    }

    @Test(description = "View customer via UI.", dependsOnMethods = "createPlan")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("View plan feature")
    public void viewPlan() {
        List<String> customerInfo = adminScreen.search(name).getPlans();

        Assert.assertTrue(customerInfo.contains(name) &
                customerInfo.contains(details) &
                customerInfo.contains(fee));

    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
