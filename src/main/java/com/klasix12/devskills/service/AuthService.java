package com.klasix12.devskills.service;

import com.klasix12.devskills.dto.AuthRequest;
import com.klasix12.devskills.dto.RefreshTokenRequest;
import com.klasix12.devskills.dto.TokenResponse;

public interface AuthService {
    TokenResponse login(AuthRequest req);
    TokenResponse refresh(RefreshTokenRequest req);
}
