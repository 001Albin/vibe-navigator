package com.vibenavigator.vibe_navigator_api.service;
import com.vibenavigator.vibe_navigator_api.dto.InteractionRequest;
import com.vibenavigator.vibe_navigator_api.entity.MediaItem;
import com.vibenavigator.vibe_navigator_api.entity.User;
import com.vibenavigator.vibe_navigator_api.entity.UserInteraction;
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
    private ProfileService profileService; // Re-using this to get the current user

    @Transactional
    public void recordInteraction(String userEmail, InteractionRequest request) {
        // 1. Find the user who is performing the action
        User currentUser = profileService.getCurrentUser(userEmail);

        // 2. Find the media item in our database by its external ID.
        // In a real system, if this is not found, you would fetch it from the external API (TMDb, etc.)
        // and save it first. For now, we assume the item exists.
        MediaItem mediaItem = mediaItemRepository
                .findByExternalIdAndItemType(request.getExternalItemId(), request.getItemType())
                .orElseThrow(() -> new IllegalStateException("Media item not found in our database."));

        // 3. Create a new UserInteraction entity
        UserInteraction newInteraction = new UserInteraction();
        newInteraction.setUser(currentUser);
        newInteraction.setItem(mediaItem);
        newInteraction.setInteractionType(request.getInteractionType());
        newInteraction.setRatingValue(request.getRatingValue());

        // 4. Save the interaction to the database
        interactionRepository.save(newInteraction);

        // 5. (Future Step) Publish an event to Kafka
        // We will add the Kafka logic here in the next module.
    }
}
