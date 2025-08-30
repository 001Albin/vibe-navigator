package com.vibenavigator.vibe_navigator_api.service;

import com.vibenavigator.vibe_navigator_api.dto.UpdateProfileRequest;
import com.vibenavigator.vibe_navigator_api.entity.User;
import com.vibenavigator.vibe_navigator_api.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProfileService {
    @Autowired
    private  UserRepository userRepository;

    public User getCurrentUser(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not fount with the eamil: "+email));
    }

    public User updateUserProfile(String email,UpdateProfileRequest profileDetails){
        User currentUser=getCurrentUser(email);
        // Check if the new username is provided and different
        if (StringUtils.hasText(profileDetails.getUsername()) && !profileDetails.getUsername().equals(currentUser.getUsername())) {
            // Check if the new username is already taken by another user
            if (userRepository.findByUsername(profileDetails.getUsername()).isPresent()) {
                throw new IllegalStateException("Username is already taken.");
            }
            currentUser.setUsername(profileDetails.getUsername());
        }
        if(profileDetails.getLanguages()!=null){
            currentUser.setLanguages(profileDetails.getLanguages());
        }
        return userRepository.save(currentUser);
    }

}
