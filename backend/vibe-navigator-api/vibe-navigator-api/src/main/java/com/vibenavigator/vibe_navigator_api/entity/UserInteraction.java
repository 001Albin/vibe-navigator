// In entity/UserInteraction.java
package com.vibenavigator.vibe_navigator_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "user_interactions")
public class UserInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private MediaItem item;

    @Enumerated(EnumType.STRING)
    @Column(name = "interaction", nullable = false)
    private InteractionType interactionType;

    @Column(name = "rating_value")
    private Integer ratingValue;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();
}