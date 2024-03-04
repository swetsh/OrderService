package com.swiggy.orders.service;

import static org.junit.jupiter.api.Assertions.*;

import com.swiggy.orders.exceptions.UserAlreadyExistException;
import com.swiggy.orders.model.User;
import com.swiggy.orders.repository.UserRepository;
import com.swiggy.orders.utils.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser_Success() {
        String name = "testUser";
        String password = "password";
        User user = new User(name, password, new Location("xyz"));

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(name, password, new Location("xyz"));

        assertNotNull(createdUser);
        verify(userRepository, times(1)).save(eq(createdUser));
        assertEquals(name, createdUser.getUsername());
        assertEquals(password, createdUser.getPassword());
    }

    @Test
    public void testCreateUser_UserAlreadyExists() {
        String name = "existingUser";
        String password = "password";

        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(UserAlreadyExistException.class, () -> userService.createUser(name, password, new Location()));
    }
}