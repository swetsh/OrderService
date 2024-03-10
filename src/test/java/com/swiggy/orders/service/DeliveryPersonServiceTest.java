package com.swiggy.orders.service;

import com.swiggy.orders.exceptions.DeliveryPersonAlreadyExistException;
import com.swiggy.orders.model.DeliveryPerson;
import com.swiggy.orders.repository.DeliveryPersonRepository;
import com.swiggy.orders.utils.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryPersonServiceTest {
    @Mock
    private DeliveryPersonRepository deliveryPersonRepository;

    @InjectMocks
    private DeliveryPersonService deliveryPersonService;

    @Test
    public void testCreatePerson_Success() {
        String name = "testPerson";
        DeliveryPerson deliveryPerson = new DeliveryPerson(name, new Location("xyz"));

        when(deliveryPersonRepository.save(any(DeliveryPerson.class))).thenReturn(deliveryPerson);

        DeliveryPerson createdPerson = deliveryPersonService.createDeliveryPerson(name, new Location("xyz"));

        assertNotNull(createdPerson);
        verify(deliveryPersonRepository, times(1)).save(eq(deliveryPerson));
        assertEquals(name, deliveryPerson.getUsername());
    }

    @Test
    public void testCreatePerson_PersonAlreadyExists() {
        String name = "existingUser";

        when(deliveryPersonRepository.save(any(DeliveryPerson.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DeliveryPersonAlreadyExistException.class, () -> deliveryPersonService.createDeliveryPerson(name, new Location()));
    }
}