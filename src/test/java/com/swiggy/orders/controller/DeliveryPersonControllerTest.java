package com.swiggy.orders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.orders.constant.ResponseMessages;
import com.swiggy.orders.dto.DeliveryPersonRequest;
import com.swiggy.orders.dto.DeliveryPersonResponse;
import com.swiggy.orders.exceptions.OrderAssignmentFailedException;
import com.swiggy.orders.model.DeliveryPerson;
import com.swiggy.orders.service.DeliveryPersonService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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


    @Test
    public void testGetAllDeliveryPerson() throws Exception {
        DeliveryPerson deliveryPerson = mock(DeliveryPerson.class);
        DeliveryPerson otherDeliveryPerson = mock(DeliveryPerson.class);
        List<DeliveryPerson> deliveryPeople = Arrays.asList(deliveryPerson, otherDeliveryPerson);

        when(deliveryPersonService.fetchAll()).thenReturn(deliveryPeople);
        when(deliveryPerson.toDto()).thenReturn(mock(DeliveryPersonResponse.class));
        when(otherDeliveryPerson.toDto()).thenReturn(mock(DeliveryPersonResponse.class));


        mockMvc.perform(get("/api/v1/delivery-persons"))
                .andExpect(status().isFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(deliveryPerson.getId()))
                .andExpect(jsonPath("$[1].id").value(otherDeliveryPerson.getId()));

        verify(deliveryPersonService, times(1)).fetchAll();
        verify(deliveryPersonService, times(0)).findDeliveryPersonById(anyInt());
    }

    @Test
    public void testUpdateDeliveryPersonOrderWithId_Success() throws Exception {
        DeliveryPerson deliveryPerson = mock(DeliveryPerson.class);

        when(deliveryPersonService.update(1, 1)).thenReturn(deliveryPerson);
        when(deliveryPerson.toDto()).thenReturn(mock(DeliveryPersonResponse.class));

        mockMvc.perform(put("/api/v1/delivery-persons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(1)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(deliveryPersonService, times(1)).update(eq(1), eq(1));
        verify(deliveryPersonService, never()).update(eq(1), eq(2));
        verify(deliveryPersonService, never()).update(eq(2), eq(1));
    }


    @Test
    public void testUpdateOrdersWithId_NotFound() throws Exception {
        when(deliveryPersonService.update(1, 1)).thenThrow(OrderAssignmentFailedException.class);

        mockMvc.perform(put("/api/v1/delivery-persons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(ResponseMessages.ORDER_ASSIGNMENT_FAILED));
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}