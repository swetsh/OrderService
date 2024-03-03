package com.swiggy.orders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiggy.orders.constant.ResponseMessages;
import com.swiggy.orders.dto.UserRequest;
import com.swiggy.orders.exceptions.UserAlreadyExistException;
import com.swiggy.orders.model.User;
import com.swiggy.orders.service.UserService;
import com.swiggy.orders.utils.Location;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;


    @Test
    public void testCreateUser_Success() throws Exception {
        UserRequest userRequest = new UserRequest("testUser", "password", new Location());
        User user = new User(userRequest.name(), "encodedPassword", new Location());

        when(userService.createUser(anyString(), anyString(), any(Location.class))).thenReturn(user);


        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(userRequest.name()));

        verify(userService, times(1)).createUser(anyString(), anyString(), any(Location.class));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testRegister_UserAlreadyExists() throws Exception {
        UserRequest userRequest = new UserRequest("existingUser", "password", new Location());

        when(userService.createUser(anyString(), anyString(), any(Location.class)))
                .thenThrow(UserAlreadyExistException.class);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequest)))
                .andExpect(status().isConflict())
                .andExpect(MockMvcResultMatchers.content().string(ResponseMessages.USER_ALREADY_EXIST));
    }


    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}