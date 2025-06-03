package com.klasix12.devskills.controller;

import com.klasix12.devskills.dto.UserDto;
import com.klasix12.devskills.dto.UserRegistrationRequest;
import com.klasix12.devskills.service.UserService;
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
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @PostMapping("/registration")
    public ResponseEntity<UserDto> register(
            @Valid
            @RequestBody UserRegistrationRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createUser(req));
    }
}
