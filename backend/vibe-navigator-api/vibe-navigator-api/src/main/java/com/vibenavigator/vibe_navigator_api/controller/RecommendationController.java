// In controller/RecommendationController.java
package com.vibenavigator.vibe_navigator_api.controller;

import com.vibenavigator.vibe_navigator_api.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @PostMapping("/now")
    public ResponseEntity<String> getImmediateRecommendation(Authentication authentication) {
        String userEmail = authentication.getName();
        recommendationService.getRecommendation(userEmail);

        // We will return a proper recommendation object later.
        String dummyResponse = "Recommendation generated for " + userEmail;
        return ResponseEntity.ok(dummyResponse);
    }
}