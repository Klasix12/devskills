package com.klasix12.devskills.dto;

import lombok.Data;

// TODO: доделать refreshToken
@Data
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
