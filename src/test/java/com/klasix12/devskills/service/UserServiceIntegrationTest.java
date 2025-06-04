package com.klasix12.devskills.service;


import com.klasix12.devskills.dto.UserDto;
import com.klasix12.devskills.dto.UserRegistrationRequest;
import com.klasix12.devskills.exception.EmailAlreadyExistsException;
import com.klasix12.devskills.exception.UsernameAlreadyExistsException;
import com.klasix12.devskills.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    void createUser_success_withDb() {
        UserRegistrationRequest request = new UserRegistrationRequest("dbuser", "denis", "pass123", "dbuser@mail.com");

        UserDto dto = userService.createUser(request);

        assertEquals("dbuser", dto.getUsername());
        assertTrue(userRepository.findByUsername("dbuser").isPresent());
    }

    @Test
    @Order(2)
    void createUser_shouldThrowWhenEmailExists() {
        userService.createUser(new UserRegistrationRequest("another123", "denis", "123", "dbuser@mail.com"));
        UserRegistrationRequest duplicate = new UserRegistrationRequest("another", "denis", "123", "dbuser@mail.com");

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(duplicate));
    }

    @Test
    @Order(3)
    void createUser_shouldThrowWhenUsernameExists() {
        userService.createUser(new UserRegistrationRequest("another", "denis", "123", "dbuser123@mail.com"));
        UserRegistrationRequest duplicate = new UserRegistrationRequest("another", "denis", "123", "dbuser@mail.com");

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.createUser(duplicate));
    }
}
