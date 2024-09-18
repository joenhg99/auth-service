package com.example.auth_service.common.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private String accessToken;
}
