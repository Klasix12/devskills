package com.klasix12.devskills.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klasix12.devskills.dto.UserDto;
import com.klasix12.devskills.dto.UserRegistrationRequest;
import com.klasix12.devskills.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockitoBean
    private UserService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private UserRegistrationRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = UserRegistrationRequest.builder()
                .username("username")
                .firstName("firstName")
                .password("12345678")
                .email("email@email.com")
                .build();
    }

    @Nested
    @DisplayName("POST /users/registration - validation tests")
    class RegistrationValidationTests {

        @Test
        @WithAnonymousUser
        @DisplayName("should return 201 and UserDto fields when request is valid")
        void shouldReturnUserDto_whenRequestIsValid() throws Exception {
            when(service.createUser(any())).thenReturn(UserDto.builder()
                            .username(validRequest.getUsername())
                            .firstName(validRequest.getFirstName())
                            .email(validRequest.getEmail())
                            .createdAt(LocalDateTime.now())
                    .build());
            mockMvc.perform(post("/users/registration")
                            .content(mapper.writeValueAsString(validRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.username", notNullValue()))
                    .andExpect(jsonPath("$.firstName", notNullValue()))
                    .andExpect(jsonPath("$.email", notNullValue()))
                    .andExpect(jsonPath("$.createdAt", notNullValue()));
        }

        @Test
        @WithAnonymousUser
        @DisplayName("should return 400 and ErrorResponseDto fields when username is blank")
        void shouldReturnErrorResponse_whenUsernameIsBlank() throws Exception {
            validRequest.setUsername("");
            performBadRequestWithErrorFields(validRequest);
        }

        @Test
        @WithAnonymousUser
        @DisplayName("should return 400 and ErrorResponseDto fields when password too short")
        void shouldReturnErrorResponse_whenPasswordTooShort() throws Exception {
            validRequest.setPassword("short");
            performBadRequestWithErrorFields(validRequest);
        }

        @Test
        @WithAnonymousUser
        @DisplayName("should return 400 and ErrorResponseDto fields when email invalid")
        void shouldReturnErrorResponse_whenEmailInvalid() throws Exception {
            validRequest.setEmail("not-an-email");
            performBadRequestWithErrorFields(validRequest);
        }

        private void performBadRequestWithErrorFields(UserRegistrationRequest request) throws Exception {
            mockMvc.perform(post("/users/registration")
                            .content(mapper.writeValueAsString(request))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", notNullValue()))
                    .andExpect(jsonPath("$.reason", notNullValue()))
                    .andExpect(jsonPath("$.status", notNullValue()))
                    .andExpect(jsonPath("$.timestamp", notNullValue()));
        }
    }
}
