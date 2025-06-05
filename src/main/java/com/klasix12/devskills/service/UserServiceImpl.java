package com.klasix12.devskills.service;

import com.klasix12.devskills.dto.RoleName;
import com.klasix12.devskills.dto.UserDto;
import com.klasix12.devskills.dto.UserRegistrationRequest;
import com.klasix12.devskills.exception.EmailAlreadyExistsException;
import com.klasix12.devskills.exception.UsernameAlreadyExistsException;
import com.klasix12.devskills.mapper.UserMapper;
import com.klasix12.devskills.model.Role;
import com.klasix12.devskills.model.User;
import com.klasix12.devskills.repository.RoleRepository;
import com.klasix12.devskills.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto createUser(UserRegistrationRequest req) {
        log.info("Creating user. email: {}, username: {}", req.getEmail(), req.getUsername());
        if (repository.findByUsername(req.getUsername()).isPresent()) {
            log.warn("User with username {} already exists.", req.getUsername());
            throw new UsernameAlreadyExistsException("User with username " + req.getUsername() + " already exists.");
        }
        if (repository.findByEmail(req.getEmail()).isPresent()) {
            log.warn("User with email {} already exists.", req.getUsername());
            throw new EmailAlreadyExistsException("User with email " + req.getEmail() + " already exists.");
        }
        User user = UserMapper.toUser(req);
        Role role = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Role " + RoleName.USER + " not found."));
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRoles(Set.of(role));
        User savedUser = repository.save(user);

        log.info("User saved successfully. ID: {}, username: {}, email: {}", savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
        return UserMapper.toUserDto(savedUser);
    }
}
