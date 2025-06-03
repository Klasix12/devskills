package com.klasix12.devskills.service;

import com.klasix12.devskills.dto.UserDto;
import com.klasix12.devskills.dto.UserRegistrationRequest;

public interface UserService {
    UserDto createUser(UserRegistrationRequest req);
}
