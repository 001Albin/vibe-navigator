package com.vibenavigator.vibe_navigator_api.repository;
import com.vibenavigator.vibe_navigator_api.entity.MediaItem;
import com.vibenavigator.vibe_navigator_api.entity.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface MediaItemRepository extends JpaRepository<MediaItem, Long> {

    // This method will be used to check if a media item already exists in our database
    // before we try to save a new one from an external API.
    Optional<MediaItem> findByExternalIdAndItemType(String externalId, MediaType itemType);
}
