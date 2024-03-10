package com.swiggy.orders.service;

import com.swiggy.orders.exceptions.DeliveryPersonAlreadyExistException;
import com.swiggy.orders.exceptions.DeliveryPersonNotFoundException;
import com.swiggy.orders.exceptions.OrderNotFoundException;
import com.swiggy.orders.model.DeliveryPerson;
import com.swiggy.orders.model.Order;
import com.swiggy.orders.repository.DeliveryPersonRepository;
import com.swiggy.orders.utils.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryPersonServiceTest {
    @Mock
    private DeliveryPersonRepository deliveryPersonRepository;


    @Mock
    private DeliveryPerson deliveryPerson;

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

    @Test
    void testFindOrderByIdShouldGiveCorrectOrders() {
        when(deliveryPersonRepository.findById(eq(1))).thenReturn(Optional.of(deliveryPerson));

        DeliveryPerson deliveryPerson = deliveryPersonService.findDeliveryPersonById(1);

        assertNotNull(deliveryPerson);
        verify(deliveryPersonRepository, times(1)).findById(eq(1));
        verify(deliveryPersonRepository, never()).findById(eq(2));
    }

    @Test
    void testFindOrderByIdShouldThrowErrorIfNotFound() {
        when(deliveryPersonRepository.findById(eq(1))).thenThrow(DeliveryPersonNotFoundException.class);

        assertThrows(DeliveryPersonNotFoundException.class, () -> deliveryPersonService.findDeliveryPersonById(1));
    }


    @Test
    void testFetchOrdersShouldGiveSavedOrders() {
        DeliveryPerson deliveryPerson1 = new DeliveryPerson();
        DeliveryPerson deliveryPerson2 = new DeliveryPerson();
        when(deliveryPersonRepository.findAll()).thenReturn(Arrays.asList(deliveryPerson1, deliveryPerson2));

        List<DeliveryPerson> deliveryPeople = deliveryPersonService.fetchAll();

        assertEquals(2, deliveryPeople.size());
        assertTrue(deliveryPeople.contains(deliveryPerson1));
        assertTrue(deliveryPeople.contains(deliveryPerson2));
    }

}