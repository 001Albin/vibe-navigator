package com.vibenavigator.vibe_navigator_api.entity;
import jakarta.persistence.*;
import lombok.Data;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash")
    private String password;

    // This will now correctly refer to the enum defined below
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;
}

