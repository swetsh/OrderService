package com.swiggy.orders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.orders.model.Order;
import com.swiggy.orders.model.User;
import com.swiggy.orders.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testCreateWallet_Success() throws Exception {
        when(orderService.create(anyList(), any(User.class))).thenReturn(new Order());

        List<Integer> menuItemIds = new ArrayList<>();

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(menuItemIds)))
                .andExpect(status().isCreated());

        verify(orderService, times(1)).create(anyList(), any(User.class));
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}