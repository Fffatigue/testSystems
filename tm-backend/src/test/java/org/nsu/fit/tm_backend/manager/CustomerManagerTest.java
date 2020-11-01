package org.nsu.fit.tm_backend.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nsu.fit.tm_backend.database.IDBService;
import org.nsu.fit.tm_backend.database.data.ContactPojo;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;
import org.nsu.fit.tm_backend.database.data.TopUpBalancePojo;
import org.nsu.fit.tm_backend.manager.auth.data.AuthenticatedUserDetails;
import org.nsu.fit.tm_backend.shared.Authority;
import org.nsu.fit.tm_backend.shared.Globals;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

// Лабораторная 2: покрыть юнит тестами класс CustomerManager на 100%.
class CustomerManagerTest {
    private Logger logger;
    private IDBService dbService;
    private CustomerManager customerManager;

    private CustomerPojo createCustomerInput;

    @BeforeEach
    void init() {
        // Создаем mock объекты.
        dbService = mock(IDBService.class);
        logger = mock(Logger.class);

        // Создаем класс, методы которого будем тестировать,
        // и передаем ему наши mock объекты.
        customerManager = new CustomerManager(dbService, logger);
    }

    @Test
    void testCreateCustomer() {
        // настраиваем mock.
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "Baba_Jaga";
        createCustomerInput.balance = 0;

        CustomerPojo createCustomerOutput = new CustomerPojo();
        createCustomerOutput.id = UUID.randomUUID();
        createCustomerOutput.firstName = "John";
        createCustomerOutput.lastName = "Wick";
        createCustomerOutput.login = "john_wick@gmail.com";
        createCustomerOutput.pass = "Baba_Jaga";
        createCustomerOutput.balance = 0;

        when(dbService.createCustomer(createCustomerInput)).thenReturn(createCustomerOutput);

        // Вызываем метод, который хотим протестировать
        CustomerPojo customer = customerManager.createCustomer(createCustomerInput);

        // Проверяем результат выполенния метода
        assertEquals(customer.id, createCustomerOutput.id);

        // Проверяем, что метод по созданию Customer был вызван ровно 1 раз с определенными аргументами
        verify(dbService, times(1)).createCustomer(createCustomerInput);

        verify(dbService, times(1)).getCustomers();

        verifyNoMoreInteractions(dbService);
    }

    // Как не надо писать тест...
    @Test
    void testCreateCustomerWithNullArgument_Wrong() {
        try {
            customerManager.createCustomer(null);
        } catch (IllegalArgumentException ex) {
            assertEquals("Argument 'customer' is null.", ex.getMessage());
        }
    }

    @Test
    void testCreateCustomerWithNullArgument_Right() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                customerManager.createCustomer(null));
        assertEquals("Argument 'customer' is null.", exception.getMessage());
    }

    @Test
    void testCreateCustomerWithShortPassword() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "123qwe";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password is very easy.", exception.getMessage());
    }

    @Test
    void testCreateCustomerWithLongPassword() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "123111111111111111qwe";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Password's length should be more or equal 6 symbols and less or equal 12 symbols.",
                exception.getMessage());
    }

    @Test
    void testCreateCustomerWithNullPassword() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = null;
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Field 'customer.pass' is null.", exception.getMessage());
    }

    @Test
    void testCreateCustomerLoginExists() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "Baba_Jaga";
        createCustomerInput.balance = 0;

        List<CustomerPojo> customerPojos = new ArrayList<>();
        customerPojos.add(createCustomerInput);
        when(dbService.getCustomers()).thenReturn(customerPojos);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Login already exists.", exception.getMessage());
    }

    @Test
    void testCreateCustomerPasswordStartsWithLowerCase() {
        createCustomerInput = new CustomerPojo();
        createCustomerInput.firstName = "John";
        createCustomerInput.lastName = "Wick";
        createCustomerInput.login = "john_wick@gmail.com";
        createCustomerInput.pass = "baba_Jaga";
        createCustomerInput.balance = 0;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> customerManager.createCustomer(createCustomerInput));
        assertEquals("Pass must starts with uppercase character.", exception.getMessage());
    }

    @Test
    void testGetCustomers() {
        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.firstName = "John";
        customerPojo.lastName = "Wick";
        customerPojo.login = "john_wick@gmail.com";
        customerPojo.pass = "123qwe";
        customerPojo.balance = 0;

        List<CustomerPojo> customerPojos = new ArrayList<>();
        customerPojos.add(customerPojo);
        when(dbService.getCustomers()).thenReturn(customerPojos);

        assertEquals(customerPojos, customerManager.getCustomers());

        verify(dbService, times(1)).getCustomers();
        verifyNoMoreInteractions(dbService);
    }

    @Test
    void testGetCustomer() {
        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.firstName = "John";
        customerPojo.lastName = "Wick";
        customerPojo.login = "john_wick@gmail.com";
        customerPojo.pass = "123qwe";
        customerPojo.balance = 0;
        UUID uuid = new UUID(100, 100);
        when(dbService.getCustomer(uuid)).thenReturn(customerPojo);

        assertEquals(customerPojo, customerManager.getCustomer(uuid));
    }

    @Test
    void testLookupCustomerNotExists() {
        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.firstName = "John";
        customerPojo.lastName = "Wick";
        customerPojo.login = "john_wick@gmail.com";
        customerPojo.pass = "123qwe";
        customerPojo.balance = 0;

        List<CustomerPojo> customerPojos = new ArrayList<>();
        customerPojos.add(customerPojo);
        when(dbService.getCustomers()).thenReturn(customerPojos);

        assertNull(customerManager.lookupCustomer("notExistedLogin"));
    }

    @Test
    void testLookupCustomerExists() {
        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.firstName = "John";
        customerPojo.lastName = "Wick";
        customerPojo.login = "john_wick@gmail.com";
        customerPojo.pass = "123qwe";
        customerPojo.balance = 0;

        List<CustomerPojo> customerPojos = new ArrayList<>();
        customerPojos.add(customerPojo);
        when(dbService.getCustomers()).thenReturn(customerPojos);

        assertEquals(customerPojo, customerManager.lookupCustomer("john_wick@gmail.com"));
    }

    @Test
    void testMeAdmin() {
        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.firstName = "John";
        customerPojo.lastName = "Wick";
        customerPojo.login = "admin";
        customerPojo.pass = "123qwe";
        customerPojo.balance = 0;

        Set<String> authorities = new HashSet<>();
        authorities.add(Authority.ADMIN_ROLE);
        ContactPojo actual = customerManager.me(new AuthenticatedUserDetails("1", "check",
                authorities));
        assertTrue(StringUtils.isEmpty(actual.pass));

        assertEquals(Globals.ADMIN_LOGIN, actual.login);
    }

    @Test
    void testMeNotAdmin() {
        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.firstName = "John";
        customerPojo.lastName = "Wick";
        customerPojo.login = "john_wick@gmail.com";
        customerPojo.pass = "123qwe";
        customerPojo.balance = 0;
        when(dbService.getCustomerByLogin("john_wick@gmail.com")).thenReturn(customerPojo);

        Set<String> authorities = new HashSet<>();
        authorities.add(Authority.CUSTOMER_ROLE);
        ContactPojo actual = customerManager.me(new AuthenticatedUserDetails("1", "john_wick@gmail.com",
                authorities));
        assertTrue(StringUtils.isEmpty(actual.pass));

        assertEquals("john_wick@gmail.com", actual.login);
    }

    @Test
    void testDeleteCustomer() {
        UUID uuid = new UUID(100, 100);
        customerManager.deleteCustomer(uuid);

        verify(dbService, times(1)).deleteCustomer(uuid);
        verifyNoMoreInteractions(dbService);
    }

    @Test
    void testTopUpBalance() {
        CustomerPojo customerPojo = new CustomerPojo();
        customerPojo.firstName = "John";
        customerPojo.lastName = "Wick";
        customerPojo.login = "john_wick@gmail.com";
        customerPojo.pass = "123qwe";
        customerPojo.balance = 0;
        UUID uuid = new UUID(100, 100);
        when(dbService.getCustomer(uuid)).thenReturn(customerPojo);

        TopUpBalancePojo topUpBalancePojo = new TopUpBalancePojo();
        topUpBalancePojo.customerId = uuid;
        topUpBalancePojo.money = 10;

        CustomerPojo actual = customerManager.topUpBalance(topUpBalancePojo);
        verify(dbService, times(1)).getCustomer(uuid);
        assertEquals(10, actual.balance);

        verify(dbService, times(1)).editCustomer(actual);

        verifyNoMoreInteractions(dbService);
    }
}
