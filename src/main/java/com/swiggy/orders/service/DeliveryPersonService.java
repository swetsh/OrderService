package com.swiggy.orders.service;

import com.swiggy.orders.exceptions.DeliveryPersonAlreadyExistException;
import com.swiggy.orders.exceptions.DeliveryPersonNotFoundException;
import com.swiggy.orders.exceptions.OrderNotFoundException;
import com.swiggy.orders.model.DeliveryPerson;
import com.swiggy.orders.model.Order;
import com.swiggy.orders.repository.DeliveryPersonRepository;
import com.swiggy.orders.state.OrderStatus;
import com.swiggy.orders.utils.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

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


    public DeliveryPerson findDeliveryPersonById(int id) {
        return deliveryPersonRepository.findById(id)
                .orElseThrow(DeliveryPersonNotFoundException::new);
    }

    public List<DeliveryPerson> fetchAll() {
        return deliveryPersonRepository.findAll();
    }

}
