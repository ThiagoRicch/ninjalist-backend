package com.example.ninjalist.config;

import lombok.Builder;

@Builder
public record JWTUserData(Long userId, String email)  {
}
