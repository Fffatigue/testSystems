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

public class CreateCustomerTest {
    private Browser browser = null;
    private AdminScreen adminScreen = null;

    private String firstName = null;
    private String lastName = null;
    private String emailAddress = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Create customer via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create customer feature")
    public void createCustomer() {
        Faker faker = new Faker();
        String password = faker.internet().password();

        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        emailAddress = faker.internet().emailAddress();

        adminScreen = new LoginScreen(browser)
                .loginAsAdmin()
                .createCustomer()
                .fillEmail(emailAddress)
                .fillPassword(password.length() > 12 ? password.substring(0, 12) : password)
                .fillFirstName(firstName)
                .fillLastName(lastName)
                .clickSubmit();
    }

    @Test(description = "View customer via UI.", dependsOnMethods = "createCustomer")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("View customer feature")
    public void viewCustomer() {
        List<String> customerInfo = adminScreen.search(firstName).getCustomers();

        Assert.assertTrue(customerInfo.contains(emailAddress) &
                customerInfo.contains(firstName) &
                customerInfo.contains(lastName));

    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
