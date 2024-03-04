package com.swiggy.orders.controller;

import com.swiggy.orders.constant.ResponseMessages;
import com.swiggy.orders.dto.OrderResponse;
import com.swiggy.orders.exceptions.OrderNotFoundException;
import com.swiggy.orders.model.Order;
import com.swiggy.orders.model.User;
import com.swiggy.orders.repository.UserRepository;
import com.swiggy.orders.service.OrderService;
import com.swiggy.orders.state.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("")
    public ResponseEntity<Object> fetchALl() {
        List<Order> orders = orderService.fetchAll();

        List<OrderResponse> orderResponses = new ArrayList<OrderResponse>();
        for (Order order : orders) {
            orderResponses.add(order.toDto());
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(orderResponses);
    }


    @GetMapping("/{orderID}")
    public ResponseEntity<Object> findOrderById(@PathVariable("orderID") int orderId) {

        try {
            Order order = orderService.findOrderById(orderId);
            return ResponseEntity.status(HttpStatus.FOUND).body(order.toDto());
        } catch (OrderNotFoundException orderNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseMessages.ORDER_NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{orderID}")
    public ResponseEntity<Object> update(@PathVariable("orderID") int orderId, @RequestBody OrderStatus orderStatus) {
        try {
            Order order = orderService.update(orderId, orderStatus);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(order.toDto());
        } catch (OrderNotFoundException orderNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseMessages.ORDER_NOT_FOUND);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName());
    }
}
