package com.swiggy.orders.dto;

import com.swiggy.orders.utils.Location;

public record UserRequest(String name, String password, Location location) {
}
