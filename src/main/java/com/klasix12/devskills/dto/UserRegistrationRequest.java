package com.klasix12.devskills.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequest {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @Size(min = 3, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @Email
    @Size(min = 6, max = 256)
    private String email;
}