package com.klasix12.devskills.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klasix12.devskills.dto.*;
import com.klasix12.devskills.service.impl.AuthServiceImpl;
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

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthServiceImpl authService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private UserRegistrationRequest validRegistrationRequest;
    private AuthRequest validAuthRequest;
    private RefreshTokenRequest validRefreshTokenRequest;

    @BeforeEach
    void setUp() {
        validRegistrationRequest = UserRegistrationRequest.builder()
                .username("username")
                .firstName("firstName")
                .password("12345678")
                .email("email@email.com")
                .build();

        validAuthRequest = new AuthRequest("username", "password");
        validRefreshTokenRequest = new RefreshTokenRequest("refreshToken");
    }

    @Nested
    @DisplayName("POST /auth/registration - validation tests")
    class RegistrationValidationTests {

        @Test
        @WithAnonymousUser
        @DisplayName("should return 201 and UserDto fields when request is valid")
        void shouldReturnUserDto_whenRequestIsValid() throws Exception {
            when(userService.createUser(any())).thenReturn(UserDto.builder()
                    .username(validRegistrationRequest.getUsername())
                    .firstName(validRegistrationRequest.getFirstName())
                    .email(validRegistrationRequest.getEmail())
                    .createdAt(LocalDateTime.now())
                    .build());
            mockMvc.perform(post("/auth/registration")
                            .content(mapper.writeValueAsString(validRegistrationRequest))
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
            validRegistrationRequest.setUsername("");
            performBadRequestWithErrorFields(validRegistrationRequest);
        }

        @Test
        @WithAnonymousUser
        @DisplayName("should return 400 and ErrorResponseDto fields when password too short")
        void shouldReturnErrorResponse_whenPasswordTooShort() throws Exception {
            validRegistrationRequest.setPassword("short");
            performBadRequestWithErrorFields(validRegistrationRequest);
        }

        @Test
        @WithAnonymousUser
        @DisplayName("should return 400 and ErrorResponseDto fields when email invalid")
        void shouldReturnErrorResponse_whenEmailInvalid() throws Exception {
            validRegistrationRequest.setEmail("not-an-email");
            performBadRequestWithErrorFields(validRegistrationRequest);
        }

        private void performBadRequestWithErrorFields(UserRegistrationRequest request) throws Exception {
            mockMvc.perform(post("/auth/registration")
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

    @Nested
    @DisplayName("POST /auth/login - authentication tests")
    class LoginTests {

        @Test
        @WithAnonymousUser
        @DisplayName("should return 200 and TokenResponse when credentials are valid")
        void shouldReturnTokenResponse_whenCredentialsAreValid() throws Exception {
            when(authService.login(any())).thenReturn(new TokenResponse("accessToken", "refreshToken"));

            mockMvc.perform(post("/auth/login")
                            .content(mapper.writeValueAsString(validAuthRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken", notNullValue()))
                    .andExpect(jsonPath("$.refreshToken", notNullValue()));
        }

        @Test
        @WithAnonymousUser
        @DisplayName("should return 400 when username is blank")
        void shouldReturnBadRequest_whenUsernameIsBlank() throws Exception {
            validAuthRequest.setUsername("");
            performBadRequestWithErrorFields("/auth/login", validAuthRequest);
        }

        @Test
        @WithAnonymousUser
        @DisplayName("should return 400 when password is blank")
        void shouldReturnBadRequest_whenPasswordIsBlank() throws Exception {
            validAuthRequest.setPassword("");
            performBadRequestWithErrorFields("/auth/login", validAuthRequest);
        }
    }

    @Nested
    @DisplayName("POST /auth/refresh - token refresh tests")
    class RefreshTokenTests {

        @Test
        @WithAnonymousUser
        @DisplayName("should return 200 and TokenResponse when refresh token is valid")
        void shouldReturnTokenResponse_whenRefreshTokenIsValid() throws Exception {
            when(authService.refresh(any())).thenReturn(new TokenResponse("newAccessToken", "newRefreshToken"));

            mockMvc.perform(post("/auth/refresh")
                            .content(mapper.writeValueAsString(validRefreshTokenRequest))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken", notNullValue()))
                    .andExpect(jsonPath("$.refreshToken", notNullValue()));
        }

        @Test
        @WithAnonymousUser
        @DisplayName("should return 400 when refresh token is blank")
        void shouldReturnBadRequest_whenRefreshTokenIsBlank() throws Exception {
            validRefreshTokenRequest.setRefreshToken("");
            performBadRequestWithErrorFields("/auth/refresh", validRefreshTokenRequest);
        }
    }

    private void performBadRequestWithErrorFields(String url, Object request) throws Exception {
        mockMvc.perform(post(url)
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
