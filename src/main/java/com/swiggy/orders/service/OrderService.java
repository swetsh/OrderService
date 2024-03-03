package com.swiggy.orders.service;

import com.swiggy.orders.model.Order;
import com.swiggy.orders.model.User;
import com.swiggy.orders.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order create(List<Integer> menuItemsId , User user) {
        return orderRepository.save(new Order(menuItemsId, user));
    }

}
