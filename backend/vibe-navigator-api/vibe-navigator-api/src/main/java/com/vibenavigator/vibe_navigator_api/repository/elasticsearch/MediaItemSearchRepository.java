// In repository/elasticsearch/MediaItemSearchRepository.java
package com.vibenavigator.vibe_navigator_api.repository.elasticsearch;

import com.vibenavigator.vibe_navigator_api.document.MediaItemDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MediaItemSearchRepository extends ElasticsearchRepository<MediaItemDocument, String> {

    @Query("{\"script_score\": {\"query\": {\"match_all\": {}}, \"script\": {\"source\": \"cosineSimilarity(params.query_vector, 'itemVector') + 1.0\", \"params\": {\"query_vector\": ?0}}}}")
    List<MediaItemDocument> findSimilar(List<Float> userVector);
}