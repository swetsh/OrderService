package com.swiggy.orders.service;

import com.swiggy.orders.exceptions.OrderNotFoundException;
import com.swiggy.orders.model.Order;
import com.swiggy.orders.model.User;
import com.swiggy.orders.repository.OrderRepository;
import com.swiggy.orders.state.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private Order order;
    @Mock
    private User user;

    @InjectMocks
    private OrderService orderService;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testCreateOrderSavesTheOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        List<Integer> menuItemIds = new ArrayList<>();

        orderService.create(menuItemIds , user);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testFindOrderByIdShouldGiveCorrectOrders() {
        when(orderRepository.findById(eq(1))).thenReturn(Optional.of(order));

        Order order = orderService.findOrderById(1);

        assertNotNull(order);
        verify(orderRepository, times(1)).findById(eq(1));
        verify(orderRepository, never()).findById(eq(2));
    }

    @Test
    void testFindOrderByIdShouldThrowErrorIfNotFound() {
        when(orderRepository.findById(eq(1))).thenThrow(OrderNotFoundException.class);

        assertThrows(OrderNotFoundException.class, () -> orderService.findOrderById(1));
    }


    @Test
    void testFetchOrdersShouldGiveSavedOrders() {
        Order order1 = new Order();
        Order order2 = new Order();
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        List<Order> orders = orderService.fetchAll();

        assertEquals(2, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
    }

    @Test
    void testUpdateStatusShouldSetCorrectOrderStatus() {
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);


        orderService.update(1, OrderStatus.ASSIGNED);

        verify(order, times(1)).setOrderStatus(eq(OrderStatus.ASSIGNED));
        verify(order, never()).setOrderStatus(eq(OrderStatus.COMPLETED));
        verify(order, never()).setOrderStatus(eq(OrderStatus.CREATED));
        verify(orderRepository, times(1)).save(any(Order.class));
    }


}