// In service/RecommendationService.java
package com.vibenavigator.vibe_navigator_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vibenavigator.vibe_navigator_api.document.MediaItemDocument;
import com.vibenavigator.vibe_navigator_api.entity.User;
import com.vibenavigator.vibe_navigator_api.repository.elasticsearch.MediaItemSearchRepository;
import com.vibenavigator.vibe_navigator_api.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MediaItemSearchRepository searchRepository; // Inject the search repository

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Optional<MediaItemDocument> getRecommendation(String userEmail) {
        // 1. Get the User's ID
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Long userId = user.getId();

        // 2. Fetch the taste profile vector from Redis
        String redisKey = "user:" + userId + ":taste_profile";
        String profileJson = redisTemplate.opsForValue().get(redisKey);

        if (profileJson == null || profileJson.isEmpty()) {
            System.out.println("No taste profile found in Redis for user: " + userEmail);
            // In a real app, you would return a non-personalized recommendation here.
            return Optional.empty();
        }

        try {
            // 3. Convert the JSON string into a vector
            List<Float> tasteProfileVector = objectMapper.readValue(profileJson, new TypeReference<>() {});
            System.out.println("Successfully fetched taste profile for user " + userEmail);

            // 4. Use the vector to find similar items in Elasticsearch
            List<MediaItemDocument> similarItems = searchRepository.findSimilar(tasteProfileVector);

            // 5. Return the top result (or an empty Optional if no results)
            if (similarItems.isEmpty()) {
                return Optional.empty();
            }
            System.out.println("Found recommendation: " + similarItems.get(0).getTitle());
            return Optional.of(similarItems.get(0));

        } catch (JsonProcessingException e) {
            System.err.println("Error parsing taste profile JSON from Redis: " + e.getMessage());
            return Optional.empty();
        }
    }
}