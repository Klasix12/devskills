package com.klasix12.devskills.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klasix12.devskills.dto.UserDto;
import com.klasix12.devskills.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UserController.class)
public class UserController {
    @MockitoBean
    private UserService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private UserDto dto;

    @BeforeEach
    void setUp() {
        dto = UserDto.builder()
                .username("username")
                .firstName("firstName")
                .email("email@email.com")
                .build();
    }

    @Test
    void test_createUserCorrectData() throws Exception {
        mockMvc.perform(post("/users/registration")
                .content(mapper.writeValueAsString(dto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
