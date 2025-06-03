package com.klasix12.devskills.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponseDto {

    private String message;

    private String reason;

    private Integer status;

    private LocalDateTime timestamp;

}
