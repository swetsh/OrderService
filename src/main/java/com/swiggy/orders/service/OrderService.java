package com.swiggy.orders.service;

import com.swiggy.orders.exceptions.OrderNotFoundException;
import com.swiggy.orders.model.Order;
import com.swiggy.orders.model.User;
import com.swiggy.orders.repository.OrderRepository;
import com.swiggy.orders.state.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order create(List<Integer> menuItemsId , User user) {
        return orderRepository.save(new Order(menuItemsId, user));
    }

    public Order findOrderById(int id) {
        return orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
    }

    public List<Order> fetchAll() {
        return orderRepository.findAll();
    }

    public Order update(int orderId, OrderStatus orderStatus){
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);

        return orderRepository.save(order);
    }
}
