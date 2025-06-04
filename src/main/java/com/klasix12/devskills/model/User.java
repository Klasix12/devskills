package com.klasix12.devskills.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String firstName;

    private String password;

    private String email;

    // TODO: замена на enum
    private String role;

    private Boolean isConfirmed;

    private LocalDateTime createdAt;
}
