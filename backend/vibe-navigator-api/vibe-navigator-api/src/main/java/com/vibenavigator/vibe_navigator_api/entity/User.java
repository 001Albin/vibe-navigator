package com.vibenavigator.vibe_navigator_api.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;

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

    @ElementCollection // This annotation is for mapping a collection of basic types
    @CollectionTable(name = "user_languages", joinColumns = @JoinColumn(name = "user_id")) // Creates a separate table to hold the languages
    @Column(name = "language")
    private List<String> languages;
}

