package com.swiggy.orders.service;

import com.swiggy.orders.model.Order;
import com.swiggy.orders.model.User;
import com.swiggy.orders.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;

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
    void testCreateWalletSavesTheWallet() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        List<Integer> menuItemIds = new ArrayList<>();

        orderService.create(menuItemIds , user);
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}