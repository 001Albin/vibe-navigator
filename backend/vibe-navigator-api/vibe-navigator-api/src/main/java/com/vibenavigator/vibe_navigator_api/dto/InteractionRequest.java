package com.vibenavigator.vibe_navigator_api.dto;
import com.vibenavigator.vibe_navigator_api.entity.InteractionType;
import com.vibenavigator.vibe_navigator_api.entity.MediaType;
import lombok.Data;

@Data
public class InteractionRequest {
    private String externalItemId; // The ID from TMDb, Spotify, etc.
    private MediaType itemType;
    private InteractionType interactionType;
    private Integer ratingValue; // Optional, only for RATING interaction
}
