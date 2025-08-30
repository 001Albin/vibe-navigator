// In service/IndexingService.java
package com.vibenavigator.vibe_navigator_api.service;

import com.vibenavigator.vibe_navigator_api.document.MediaItemDocument;
import com.vibenavigator.vibe_navigator_api.entity.MediaItem;
import com.vibenavigator.vibe_navigator_api.repository.jpa.MediaItemRepository;
import com.vibenavigator.vibe_navigator_api.repository.elasticsearch.MediaItemSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndexingService {

    // This list MUST be identical to the one in your Python script
    private static final List<String> ALL_GENRES = Arrays.asList(
            "Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama",
            "Family", "Fantasy", "History", "Horror", "Music", "Mystery", "Romance",
            "Science Fiction", "TV Movie", "Thriller", "War", "Western"
    );

    @Autowired
    private MediaItemRepository mediaItemRepository;

    @Autowired
    private MediaItemSearchRepository mediaItemSearchRepository;

    @Transactional(readOnly = true)
    public void indexMediaItems() {
        System.out.println("Starting to index media items...");

        List<MediaItem> allItems = mediaItemRepository.findAll();
        if (allItems.isEmpty()) {
            System.out.println("No media items found in PostgreSQL to index.");
            return;
        }

        List<MediaItemDocument> documents = new ArrayList<>();
        for (MediaItem item : allItems) {
            MediaItemDocument doc = new MediaItemDocument();
            doc.setId(item.getExternalId());
            doc.setPostgresId(item.getId());
            doc.setExternalId(item.getExternalId());
            doc.setItemType(item.getItemType());
            doc.setTitle(item.getTitle());
            doc.setDescription(item.getDescription());
            doc.setReleaseDate(item.getReleaseDate());
            doc.setGenres(item.getGenres());
            doc.setLanguage(item.getLanguage());

            // Calculate and set the item's vector
            if (item.getGenres() != null) {
                doc.setItemVector(vectorizeGenres(item.getGenres()));
            }
            documents.add(doc);
        }

        mediaItemSearchRepository.saveAll(documents);
        System.out.println("Successfully indexed " + documents.size() + " media items with vectors.");
    }

    private List<Float> vectorizeGenres(List<String> genres) {
        List<Float> vector = new ArrayList<>();
        for (String genre : ALL_GENRES) {
            if (genres.contains(genre)) {
                vector.add(1.0f);
            } else {
                vector.add(0.0f);
            }
        }
        return vector;
    }
}