package com.klasix12.devskills.controller;

import com.klasix12.devskills.dto.AuthRequest;
import com.klasix12.devskills.dto.TokenResponse;
import com.klasix12.devskills.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody AuthRequest req) {
        return ResponseEntity.ok().body(authService.login(req));
    }
}
