package com.swiggy.orders.dto;

import com.swiggy.orders.utils.Location;

public record DeliveryPersonRequest(String name, Location location) {
}
