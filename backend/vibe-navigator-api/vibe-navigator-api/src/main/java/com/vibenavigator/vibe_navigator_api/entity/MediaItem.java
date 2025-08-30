// In entity/MediaItem.java
package com.vibenavigator.vibe_navigator_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "media_items")

public class MediaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private MediaType itemType;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @ElementCollection
    @CollectionTable(name = "media_item_genres", joinColumns = @JoinColumn(name = "media_item_id"))
    @Column(name = "genre")
    private List<String> genres;

    private String language;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt = ZonedDateTime.now();
}