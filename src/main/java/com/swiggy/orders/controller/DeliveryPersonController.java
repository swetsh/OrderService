package com.swiggy.orders.controller;

import com.swiggy.orders.constant.ResponseMessages;
import com.swiggy.orders.dto.DeliveryPersonRequest;
import com.swiggy.orders.dto.UserRequest;
import com.swiggy.orders.exceptions.DeliveryPersonAlreadyExistException;
import com.swiggy.orders.exceptions.UserAlreadyExistException;
import com.swiggy.orders.model.DeliveryPerson;
import com.swiggy.orders.model.User;
import com.swiggy.orders.service.DeliveryPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
