package com.vibenavigator.vibe_navigator_api.repository.jpa;
import com.vibenavigator.vibe_navigator_api.entity.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInteractionRepository extends JpaRepository<UserInteraction, Long> {
    // We don't need any custom methods for now.
    // The built-in .save() method is all we need to log new interactions.
}