package com.klasix12.devskills.service;

import com.klasix12.devskills.dto.RoleName;
import com.klasix12.devskills.dto.UserDto;
import com.klasix12.devskills.dto.UserRegistrationRequest;
import com.klasix12.devskills.exception.EmailAlreadyExistsException;
import com.klasix12.devskills.exception.UsernameAlreadyExistsException;
import com.klasix12.devskills.model.Role;
import com.klasix12.devskills.model.User;
import com.klasix12.devskills.repository.RoleRepository;
import com.klasix12.devskills.repository.UserRepository;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_success() {
        UserRegistrationRequest request = new UserRegistrationRequest("newuser", "Denis", "12345678", "email@email.com");
        Role role = new Role(1L, RoleName.USER, "description");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("email@email.com")).thenReturn(Optional.empty());
        when(roleRepository.findByName(RoleName.USER)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("12345678")).thenReturn("hashed");

        User savedUser = User.builder()
                .id(1L)
                .username("newuser")
                .firstName("Denis")
                .password("hashed")
                .email("test@example.com")
                .roles(Set.of(role))
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto dto = userService.createUser(request);

        assertEquals("newuser", dto.getUsername());
        assertEquals("test@example.com", dto.getEmail());
    }

    @Test
    void createUser_usernameAlreadyExists() {
        UserRegistrationRequest request = new UserRegistrationRequest("existing", "denis", "123", "new@mail.com");

        when(userRepository.findByUsername("existing")).thenReturn(Optional.of(new User()));

        assertThrows(UsernameAlreadyExistsException.class, () -> userService.createUser(request));
    }

    @Test
    void createUser_emailAlreadyExists() {
        UserRegistrationRequest request = new UserRegistrationRequest("newuser", "denis", "123", "exist@mail.com");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("exist@mail.com")).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(request));
    }
}
