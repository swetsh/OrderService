package com.swiggy.orders.dto;

import com.swiggy.orders.model.Order;
import com.swiggy.orders.model.User;
import com.swiggy.orders.state.OrderStatus;
import com.swiggy.orders.utils.Location;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {
    private int id;

    private int userID;

    private List<Integer> menuItemIds;

    private OrderStatus orderStatus;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.menuItemIds = order.getMenuItemsIds();
        this.orderStatus = order.getOrderStatus();
        this.userID = order.getUser().getId();
    }

}
