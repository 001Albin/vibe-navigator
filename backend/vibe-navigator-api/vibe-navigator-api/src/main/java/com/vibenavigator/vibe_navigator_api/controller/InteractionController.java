package com.vibenavigator.vibe_navigator_api.controller;
import com.vibenavigator.vibe_navigator_api.dto.InteractionRequest;
import com.vibenavigator.vibe_navigator_api.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interactions")
public class InteractionController {
    @Autowired
    private InteractionService interactionService;

    @PostMapping
    public ResponseEntity<Void> logInteraction(Authentication authentication, @RequestBody InteractionRequest request) {
        String userEmail = authentication.getName();
        interactionService.recordInteraction(userEmail, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
