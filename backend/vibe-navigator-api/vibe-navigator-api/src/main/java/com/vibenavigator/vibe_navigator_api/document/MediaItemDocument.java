// In document/MediaItemDocument.java
package com.vibenavigator.vibe_navigator_api.document;

import com.vibenavigator.vibe_navigator_api.entity.MediaType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.LocalDate;
import java.util.List;

@Data
@Document(indexName = "media_items")
public class MediaItemDocument {

    @Id
    private String id;

    @Field(type = FieldType.Long)
    private Long postgresId;

    @Field(type = FieldType.Text)
    private String externalId;

    @Field(type = FieldType.Keyword)
    private MediaType itemType;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Date)
    private LocalDate releaseDate;

    @Field(type = FieldType.Keyword)
    private List<String> genres;

    @Field(type = FieldType.Keyword)
    private String language;

    // Add this new field for the vector
    @Field(type = FieldType.Dense_Vector, dims = 19)
    private List<Float> itemVector;
}