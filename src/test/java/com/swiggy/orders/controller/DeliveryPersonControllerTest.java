package com.swiggy.orders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.orders.dto.DeliveryPersonRequest;
import com.swiggy.orders.dto.UserRequest;
import com.swiggy.orders.model.DeliveryPerson;
import com.swiggy.orders.model.User;
import com.swiggy.orders.service.DeliveryPersonService;
import com.swiggy.orders.service.UserService;
import com.swiggy.orders.utils.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class DeliveryPersonControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryPersonService deliveryPersonService;

    @InjectMocks
    private DeliveryPersonController deliveryPersonController;

    @Test
    public void testCreateUser_Success() throws Exception {
        DeliveryPersonRequest deliveryPersonRequest = new DeliveryPersonRequest("testUser", new Location());
        DeliveryPerson deliveryPerson = new DeliveryPerson(deliveryPersonRequest.name(), deliveryPersonRequest.location());

        when(deliveryPersonService.createDeliveryPerson(anyString(), any(Location.class))).thenReturn(deliveryPerson);


        mockMvc.perform(post("/api/v1/delivery-persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(deliveryPersonRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(deliveryPersonRequest.name()));

        verify(deliveryPersonService, times(1)).createDeliveryPerson(anyString(), any(Location.class));
    }


    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}