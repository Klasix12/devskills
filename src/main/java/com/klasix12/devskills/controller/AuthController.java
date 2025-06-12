package com.klasix12.devskills.controller;

import com.klasix12.devskills.annotation.DefaultApiResponses;
import com.klasix12.devskills.dto.*;
import com.klasix12.devskills.service.AuthService;
import com.klasix12.devskills.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication endpoints include jwt")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @DefaultApiResponses(summary = "Login a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthRequest req) {
        return ResponseEntity
                .ok()
                .body(authService.login(req));
    }

    @DefaultApiResponses(summary = "Refresh access and refresh tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class)))
    })
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest req) {
        return ResponseEntity
                .ok()
                .body(authService.refresh(req));
    }

    @DefaultApiResponses(summary = "Register a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success",
                    content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    @PostMapping("/registration")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserRegistrationRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(req));
    }
}
