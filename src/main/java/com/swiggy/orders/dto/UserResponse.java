package com.swiggy.orders.dto;


import com.swiggy.orders.model.User;
import com.swiggy.orders.utils.Location;
import lombok.Data;

@Data
public class UserResponse {
    private int id;
    private String username;

    private Location location;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.location = user.getLocation();
    }

}
