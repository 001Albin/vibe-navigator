// In controller/IndexingController.java
package com.vibenavigator.vibe_navigator_api.controller;

import com.vibenavigator.vibe_navigator_api.service.IndexingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/index")
public class IndexingController {

    @Autowired
    private IndexingService indexingService;

    @PostMapping
    public ResponseEntity<String> triggerIndexing() {
        indexingService.indexMediaItems();
        return ResponseEntity.ok("Indexing process started successfully.");
    }
}