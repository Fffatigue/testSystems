package org.nsu.fit.tm_backend.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.nsu.fit.tm_backend.database.data.CustomerPojo;
import org.nsu.fit.tm_backend.database.data.SubscriptionPojo;
import org.nsu.fit.tm_backend.manager.CustomerManager;
import org.nsu.fit.tm_backend.manager.SubscriptionManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StatisticOperationTest {
    private CustomerManager customerManager = Mockito.mock(CustomerManager.class);
    private SubscriptionManager subscriptionManager = Mockito.mock(SubscriptionManager.class);

    // Лабораторная 2: покрыть юнит тестами класс StatisticOperation на 100%.
    @Test
    void testCreateStatisticOperationWithNullCustomerManager() {
        List<UUID> uuids = new ArrayList<>();
        uuids.add(UUID.randomUUID());
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new StatisticOperation(null, subscriptionManager, uuids));
        assertEquals("customerManager", exception.getMessage());
    }

    @Test
    void testCreateStatisticOperationWithNullSubscriptionManager() {
        List<UUID> uuids = new ArrayList<>();
        uuids.add(UUID.randomUUID());
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new StatisticOperation(customerManager, null, uuids));
        assertEquals("subscriptionManager", exception.getMessage());
    }

    @Test
    void testCreateStatisticOperationWithNullIds() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> new StatisticOperation(customerManager, subscriptionManager, null));
        assertEquals("customerIds", exception.getMessage());
    }

    @Test
    void testCreateStatisticOperationExecute() {
        List<UUID> uuids = new ArrayList<>();

        CustomerPojo customerPojo1 = new CustomerPojo();
        customerPojo1.firstName = "John1";
        customerPojo1.lastName = "Wick1";
        customerPojo1.login = "john_wick@gmail.com1";
        customerPojo1.pass = "123qwe1";
        customerPojo1.balance = 100;
        UUID uuid1 = UUID.randomUUID();
        Mockito.when(customerManager.getCustomer(uuid1)).thenReturn(customerPojo1);
        uuids.add(uuid1);

        SubscriptionPojo subscriptionPojo1 = new SubscriptionPojo();
        subscriptionPojo1.customerId = uuid1;
        subscriptionPojo1.planDetails = "details1";
        subscriptionPojo1.planFee = 10;
        subscriptionPojo1.planName = "planName1";
        subscriptionPojo1.id = UUID.randomUUID();
        subscriptionPojo1.planId = UUID.randomUUID();
        List<SubscriptionPojo> subscriptionPojos = new ArrayList<>();
        subscriptionPojos.add(subscriptionPojo1);
        Mockito.when(subscriptionManager.getSubscriptions(uuid1)).thenReturn(subscriptionPojos);

        CustomerPojo customerPojo2 = new CustomerPojo();
        customerPojo2.firstName = "John2";
        customerPojo2.lastName = "Wick2";
        customerPojo2.login = "john_wick@gmail.com2";
        customerPojo2.pass = "123qwe2";
        customerPojo2.balance = 200;
        UUID uuid2 = UUID.randomUUID();
        Mockito.when(customerManager.getCustomer(uuid2)).thenReturn(customerPojo2);
        uuids.add(uuid2);

        SubscriptionPojo subscriptionPojo2 = new SubscriptionPojo();
        subscriptionPojo2.customerId = uuid2;
        subscriptionPojo2.planDetails = "details2";
        subscriptionPojo2.planFee = 20;
        subscriptionPojo2.planName = "planName2";
        subscriptionPojo2.id = UUID.randomUUID();
        subscriptionPojo2.planId = UUID.randomUUID();
        SubscriptionPojo subscriptionPojo3 = new SubscriptionPojo();
        subscriptionPojo3.customerId = uuid2;
        subscriptionPojo3.planDetails = "details3";
        subscriptionPojo3.planFee = 30;
        subscriptionPojo3.planName = "planName3";
        subscriptionPojo3.id = UUID.randomUUID();
        subscriptionPojo3.planId = UUID.randomUUID();
        subscriptionPojos = new ArrayList<>();
        subscriptionPojos.add(subscriptionPojo2);
        subscriptionPojos.add(subscriptionPojo3);
        Mockito.when(subscriptionManager.getSubscriptions(uuid2)).thenReturn(subscriptionPojos);

        StatisticOperation statisticOperation = new StatisticOperation(customerManager, subscriptionManager, uuids);
        StatisticOperation.StatisticOperationResult statisticOperationResult = statisticOperation.Execute();
        Assertions.assertEquals(300, statisticOperationResult.overallBalance);
        Assertions.assertEquals(60, statisticOperationResult.overallFee);
    }
}
