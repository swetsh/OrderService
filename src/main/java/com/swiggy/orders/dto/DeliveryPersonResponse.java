package com.swiggy.orders.dto;

import com.swiggy.orders.model.DeliveryPerson;
import com.swiggy.orders.model.Order;
import com.swiggy.orders.utils.Location;
import lombok.Data;

@Data
public class DeliveryPersonResponse {
    private int id;
    private String name;

    private Location location;

    private int orderId;

    public DeliveryPersonResponse(DeliveryPerson deliveryPerson) {
        this.id = deliveryPerson.getId();
        this.name = deliveryPerson.getUsername();
        this.location = deliveryPerson.getLocation();
        Order order = deliveryPerson.getOrder();
        if (order == null)
            this.orderId = -1;
        else
            this.orderId = order.getId();
    }
}
