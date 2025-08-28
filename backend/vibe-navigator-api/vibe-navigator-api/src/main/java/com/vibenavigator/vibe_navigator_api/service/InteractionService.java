// In service/InteractionService.java
package com.vibenavigator.vibe_navigator_api.service;

import com.vibenavigator.vibe_navigator_api.dto.InteractionRequest;
import com.vibenavigator.vibe_navigator_api.entity.MediaItem;
import com.vibenavigator.vibe_navigator_api.entity.User;
import com.vibenavigator.vibe_navigator_api.entity.UserInteraction;
import com.vibenavigator.vibe_navigator_api.kafka.KafkaProducerService; // Import the new service
import com.vibenavigator.vibe_navigator_api.repository.MediaItemRepository;
import com.vibenavigator.vibe_navigator_api.repository.UserInteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InteractionService {

    @Autowired
    private UserInteractionRepository interactionRepository;

    @Autowired
    private MediaItemRepository mediaItemRepository;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private KafkaProducerService kafkaProducerService; // Inject the Kafka producer

    @Transactional
    public void recordInteraction(String userEmail, InteractionRequest request) {
        // 1. Find the user
        User currentUser = profileService.getCurrentUser(userEmail);

        // 2. Find the media item
        MediaItem mediaItem = mediaItemRepository
                .findByExternalIdAndItemType(request.getExternalItemId(), request.getItemType())
                .orElseThrow(() -> new IllegalStateException("Media item not found in our database."));

        // 3. Create and save the new interaction
        UserInteraction newInteraction = new UserInteraction();
        newInteraction.setUser(currentUser);
        newInteraction.setItem(mediaItem);
        newInteraction.setInteractionType(request.getInteractionType());
        newInteraction.setRatingValue(request.getRatingValue());
        interactionRepository.save(newInteraction);

        // 4. Publish an event to Kafka
        String message = String.format("{\"userId\": %d, \"itemId\": %d, \"interactionType\": \"%s\"}",
                currentUser.getId(),
                mediaItem.getId(),
                request.getInteractionType().toString());
        kafkaProducerService.sendInteractionEvent(message);
    }
}