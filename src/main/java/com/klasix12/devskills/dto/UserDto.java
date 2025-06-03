package com.klasix12.devskills.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserDto {

    private String username;

    private String firstName;

    private String email;

    private LocalDateTime createdAt;
}
