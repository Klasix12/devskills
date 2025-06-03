package com.klasix12.devskills.mapper;

import com.klasix12.devskills.dto.UserDto;
import com.klasix12.devskills.dto.UserRegistrationRequest;
import com.klasix12.devskills.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static User toUser(UserRegistrationRequest req) {
        return User.builder()
                .username(req.getUsername())
                .firstName(req.getFirstName())
                .password(req.getPassword())
                .email(req.getEmail())
                .role("USER")
                .isConfirmed(false)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
