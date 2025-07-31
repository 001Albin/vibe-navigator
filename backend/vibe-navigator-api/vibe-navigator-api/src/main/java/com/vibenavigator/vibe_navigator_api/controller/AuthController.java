package com.vibenavigator.vibe_navigator_api.controller;
import com.vibenavigator.vibe_navigator_api.dto.LoginRequest;
import com.vibenavigator.vibe_navigator_api.dto.SignUpRequest;
import com.vibenavigator.vibe_navigator_api.entity.User;
import com.vibenavigator.vibe_navigator_api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    // We will inject an AuthService here later to handle the logic
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        // Call the service to handle the registration logic
        User savedUser = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(savedUser); // Return the saved user object
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Logic to authenticate and return a JWT will go here
        return ResponseEntity.ok("User logged in successfully!");
    }
}
