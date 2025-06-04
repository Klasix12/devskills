package com.klasix12.devskills.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Request object for user registration")
public class UserRegistrationRequest {

    @Schema(
            description = "Unique username (3-50 characters)",
            example = "klasix12",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @Schema(description = "First name of the user (optional, 3-50 characters)",
            example = "artem",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(min = 3, max = 50)
    private String firstName;

    @Schema(description = "User's password (min 8 characters)",
            example = "ReallyStr0ngPassword",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;

    @Schema(description = "Valid email address",
            example = "email@email.com",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Email
    @Size(min = 6, max = 256)
    private String email;
}