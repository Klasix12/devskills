package com.klasix12.devskills.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "User DTO for responses")
public class UserDto {

    @Schema(description = "Username", example = "klasix12")
    private String username;

    @Schema(description = "First name", example = "Artem")
    private String firstName;

    @Schema(description = "Email", example = "test@test.com")
    private String email;

    @Schema(description = "Date and time of account creation", example = "")
    private LocalDateTime createdAt;
}
