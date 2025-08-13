package com.vibenavigator.vibe_navigator_api.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProfileResponse {
    private Long id;
    private String email;
    private String username;
    private List<String>languages;
}
