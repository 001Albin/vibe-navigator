package com.vibenavigator.vibe_navigator_api.dto;
import lombok.Data;
@Data
public class SignUpRequest {
    private String email;
    private String username;
    private String password;
}
