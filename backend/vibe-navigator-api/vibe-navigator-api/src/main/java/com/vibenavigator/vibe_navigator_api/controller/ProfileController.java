package com.vibenavigator.vibe_navigator_api.controller;

import com.vibenavigator.vibe_navigator_api.dto.ProfileResponse;
import com.vibenavigator.vibe_navigator_api.dto.UpdateProfileRequest;
import com.vibenavigator.vibe_navigator_api.entity.User;
import com.vibenavigator.vibe_navigator_api.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileResponse> getCurrentUserProfile(Authentication authentication) {
        // ... existing code ...
        User currentUser = profileService.getCurrentUser(authentication.getName());

        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setId(currentUser.getId());
        profileResponse.setEmail(currentUser.getEmail());
        profileResponse.setUsername(currentUser.getUsername());

        return ResponseEntity.ok(profileResponse);
    }

    // Add this new endpoint
    @PutMapping
    public ResponseEntity<ProfileResponse> updateUserProfile(Authentication authentication, @RequestBody UpdateProfileRequest updateRequest) {
        User updatedUser = profileService.updateUserProfile(authentication.getName(), updateRequest);

        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setId(updatedUser.getId());
        profileResponse.setEmail(updatedUser.getEmail());
        profileResponse.setUsername(updatedUser.getUsername());

        return ResponseEntity.ok(profileResponse);
    }
}
