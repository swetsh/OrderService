package com.swiggy.orders.controller;

import com.swiggy.orders.model.Order;
import com.swiggy.orders.model.User;
import com.swiggy.orders.repository.UserRepository;
import com.swiggy.orders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("")
    public ResponseEntity<Object> create(@RequestBody List<Integer> menuItemsId) {
        User user = getAuthenticatedUser();

        Order order = orderService.create(menuItemsId, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(order.toDto());
    }


    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName());
    }
}
