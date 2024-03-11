package com.swiggy.orders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.orders.constant.ResponseMessages;
import com.swiggy.orders.dto.OrderResponse;
import com.swiggy.orders.exceptions.OrderNotFoundException;
import com.swiggy.orders.model.Order;
import com.swiggy.orders.model.User;
import com.swiggy.orders.repository.OrderRepository;
import com.swiggy.orders.repository.UserRepository;
import com.swiggy.orders.service.OrderService;
import com.swiggy.orders.state.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;
    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private OrderController orderController;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testCreateOrder_Success() throws Exception {
        Order order = mock(Order.class);

        List<Integer> menuItemIds = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Integer> otherMenuItemIds = new ArrayList<>(Arrays.asList(4, 5, 6));

        when(orderService.create(eq(menuItemIds), any(User.class))).thenReturn(order);
        when(userRepository.findByUsername("user")).thenReturn(new User());
        when(order.toDto()).thenReturn(mock(OrderResponse.class));


        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(menuItemIds)))
                .andExpect(status().isCreated());

        verify(userRepository, times(1)).findByUsername(eq("user"));
        verify(userRepository, never()).findByUsername(eq("user2"));
        verify(orderService, times(1)).create(eq(menuItemIds), any(User.class));
        verify(orderService, never()).create(eq(otherMenuItemIds), any(User.class));
    }

    @Test
    @WithMockUser
    public void testGetAllOrders() throws Exception {
        Order order = mock(Order.class);
        Order otherOrder = mock(Order.class);
        List<Order> orders = Arrays.asList(order, otherOrder);

        when(orderService.fetchAll()).thenReturn(orders);
        when(order.toDto()).thenReturn(mock(OrderResponse.class));
        when(otherOrder.toDto()).thenReturn(mock(OrderResponse.class));


        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(order.getId()))
                .andExpect(jsonPath("$[1].id").value(otherOrder.getId()));

        verify(orderService, times(1)).fetchAll();
        verify(orderService, times(0)).findOrderById(anyInt());
    }

    @Test
    @WithMockUser
    public void testGetOrdersWithId() throws Exception {
        Order order = mock(Order.class);

        when(orderService.findOrderById(1)).thenReturn(order);
        when(order.toDto()).thenReturn(mock(OrderResponse.class));

        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService, times(1)).findOrderById(eq(1));
        verify(orderService, never()).findOrderById(2);
    }

    @Test
    @WithMockUser
    public void testGetOrdersWithId_NotFound() throws Exception {
        when(orderService.findOrderById(1)).thenThrow(OrderNotFoundException.class);

        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ResponseMessages.ORDER_NOT_FOUND));
    }

    @Test
    @WithMockUser
    public void testPutOrdersWithId_Success() throws Exception {
        Order order = mock(Order.class);

        when(orderService.update(1, OrderStatus.COMPLETED)).thenReturn(order);
        when(order.toDto()).thenReturn(mock(OrderResponse.class));

        mockMvc.perform(put("/api/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(OrderStatus.COMPLETED)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(orderService, times(1)).update(eq(1), eq(OrderStatus.COMPLETED));
        verify(orderService, never()).update(eq(2), eq(OrderStatus.COMPLETED));
        verify(orderService, never()).update(eq(1), eq(OrderStatus.ASSIGNED));
        verify(orderService, never()).update(eq(1), eq(OrderStatus.CREATED));
    }


    @Test
    @WithMockUser
    public void testUpdateOrdersWithId_NotFound() throws Exception {
        when(orderService.update(1, OrderStatus.COMPLETED)).thenThrow(OrderNotFoundException.class);

        mockMvc.perform(put("/api/v1/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(OrderStatus.COMPLETED)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ResponseMessages.ORDER_NOT_FOUND));
    }


    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}