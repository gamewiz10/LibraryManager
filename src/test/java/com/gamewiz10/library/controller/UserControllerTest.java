package com.gamewiz10.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamewiz10.library.config.TestSecurityConfig;
import com.gamewiz10.library.entity.User;
import com.gamewiz10.library.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User("user1", "user1@example.com");

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"));
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User("user1", "user1@example.com");
        user.setId(1L);

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"));
    }

    @Test
    public void testGetUserByUsername() throws Exception {
        User user = new User("user1", "user1@example.com");

        when(userService.getUserByUsername("user1")).thenReturn(user);

        mockMvc.perform(get("/api/users/username/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"));
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        User user = new User("user1", "user1@example.com");

        when(userService.getUserByEmail("user1@example.com")).thenReturn(user);

        mockMvc.perform(get("/api/users/email/user1@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"));
    }

    @Test
    public void testExistsByUsername() throws Exception {
        when(userService.existsByUsername("user1")).thenReturn(true);
        when(userService.existsByEmail("user1@example.com")).thenReturn(false);

        mockMvc.perform(get("/api/users/exists")
                        .param("username", "user1")
                        .param("email", "user1@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void testExistsByEmail() throws Exception {
        when(userService.existsByUsername("user1")).thenReturn(false);
        when(userService.existsByEmail("user1@example.com")).thenReturn(true);

        mockMvc.perform(get("/api/users/exists")
                        .param("username", "user1")
                        .param("email", "user1@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}