package com.swiggy.orders.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryPersonTest {

    @Test
    void testAssignOrderShouldAssignCorrectOrder() {
        Order order = new Order(Arrays.asList(1,2,3), new User());
        Order otherOrder = new Order(Arrays.asList(4,5,6), new User());

        DeliveryPerson deliveryPerson = new DeliveryPerson();

        assertNull(deliveryPerson.getOrder());
        deliveryPerson.assignOrder(order);
        assertEquals(deliveryPerson.getOrder(), order);
        assertNotEquals(deliveryPerson.getOrder(), otherOrder);
    }
}