package com.swiggy.orders.controller;

import com.swiggy.orders.constant.ResponseMessages;
import com.swiggy.orders.dto.UserRequest;
import com.swiggy.orders.exceptions.UserAlreadyExistException;
import com.swiggy.orders.model.User;
import com.swiggy.orders.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<Object> createUser(@RequestBody UserRequest userRequest) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userRequest.password());

        try {
            User user = userService.createUser(userRequest.name(),  encodedPassword, userRequest.location());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        catch (UserAlreadyExistException userAlreadyExistException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseMessages.USER_ALREADY_EXIST);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
