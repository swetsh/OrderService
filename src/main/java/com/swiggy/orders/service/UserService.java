package com.swiggy.orders.service;

import com.swiggy.orders.exceptions.UserAlreadyExistException;
import com.swiggy.orders.model.User;
import com.swiggy.orders.repository.UserRepository;
import com.swiggy.orders.utils.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(String name, String password, Location location) {
        User user = new User(name, password, location);

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new UserAlreadyExistException();
        }
    }
}
