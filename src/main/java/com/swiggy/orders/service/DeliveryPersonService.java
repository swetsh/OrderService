package com.swiggy.orders.service;

import com.swiggy.orders.exceptions.DeliveryPersonAlreadyExistException;
import com.swiggy.orders.model.DeliveryPerson;
import com.swiggy.orders.repository.DeliveryPersonRepository;
import com.swiggy.orders.utils.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class DeliveryPersonService {
    @Autowired
    private DeliveryPersonRepository deliveryPersonRepository;

    public DeliveryPerson createDeliveryPerson(String name, Location location) {
        DeliveryPerson deliveryPerson = new DeliveryPerson(name,  location);

        try {
            return deliveryPersonRepository.save(deliveryPerson);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new DeliveryPersonAlreadyExistException();
        }
    }
}
