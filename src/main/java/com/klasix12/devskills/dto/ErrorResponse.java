package com.klasix12.devskills.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Default error response")
public class ErrorResponse {

    @Schema(description = "Short error description", example = "Email already exists.")
    private String message;

    @Schema(description = "Error reason", example = "User with email email@email.com already exists.")
    private String reason;

    @Schema(description = "Error status", example = "400")
    private Integer status;

    private LocalDateTime timestamp;
}
