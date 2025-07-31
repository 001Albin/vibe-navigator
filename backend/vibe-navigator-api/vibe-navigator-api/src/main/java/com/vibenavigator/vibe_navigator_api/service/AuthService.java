package com.vibenavigator.vibe_navigator_api.service;

import com.vibenavigator.vibe_navigator_api.dto.SignUpRequest;
import com.vibenavigator.vibe_navigator_api.entity.AuthProvider;
import com.vibenavigator.vibe_navigator_api.entity.User;
import com.vibenavigator.vibe_navigator_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(SignUpRequest signUpRequest) {
        // Optional: Check if user already exists
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already in use");
        }

        // Create a new User entity
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setProvider(AuthProvider.LOCAL); // Set provider to LOCAL for email signup

        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        // Save the user to the database and return the saved entity
        return userRepository.save(user);
    }
}
