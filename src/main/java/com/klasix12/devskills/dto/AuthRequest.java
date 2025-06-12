package com.klasix12.devskills.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequest {

    @NotBlank
    @Schema(description = "Username", example = "klasix12")
    private String username;

    @NotBlank
    @Schema(description = "Password", example = "ReallyStr0ngPassword")
    private String password;
}
