package com.educonnect.dto.auth;

import lombok.Builder;

@Builder
public class AuthResponse {
    private String message;
    private String data;
    private Boolean status;
    private String token;
}
