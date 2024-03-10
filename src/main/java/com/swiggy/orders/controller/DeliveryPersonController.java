package com.swiggy.orders.controller;

import com.swiggy.orders.constant.ResponseMessages;
import com.swiggy.orders.dto.DeliveryPersonRequest;
import com.swiggy.orders.dto.DeliveryPersonResponse;
import com.swiggy.orders.exceptions.DeliveryPersonAlreadyExistException;
import com.swiggy.orders.exceptions.OrderAssignmentFailedException;
import com.swiggy.orders.model.DeliveryPerson;
import com.swiggy.orders.service.DeliveryPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/delivery-persons")
public class DeliveryPersonController {
    @Autowired
    private DeliveryPersonService deliveryPersonService;

    @PostMapping("")
    public ResponseEntity<Object> createDeliveryPerson(@RequestBody DeliveryPersonRequest deliveryPersonRequest) {

        try {
            DeliveryPerson deliveryPerson = deliveryPersonService.createDeliveryPerson(deliveryPersonRequest.name(), deliveryPersonRequest.location());
            return ResponseEntity.status(HttpStatus.CREATED).body(deliveryPerson);
        }
        catch (DeliveryPersonAlreadyExistException deliveryPersonAlreadyExistException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseMessages.DELIVERY_PERSON_ALREADY_EXIST);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("")
    public ResponseEntity<Object> fetchALl() {
        List<DeliveryPerson> deliveryPeople = deliveryPersonService.fetchAll();

        List<DeliveryPersonResponse> deliveryPersonResponses = new ArrayList<>();
        for (DeliveryPerson deliveryPerson : deliveryPeople) {
            deliveryPersonResponses.add(deliveryPerson.toDto());
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(deliveryPersonResponses);
    }

    @PutMapping("/{personID}")
    public ResponseEntity<Object> update(@PathVariable("personID") int personId, @RequestBody int orderId) {
        try {
            DeliveryPerson deliveryPerson = deliveryPersonService.update(personId, orderId);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(deliveryPerson.toDto());
        } catch (OrderAssignmentFailedException orderAssignmentFailedException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseMessages.ORDER_ASSIGNMENT_FAILED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
