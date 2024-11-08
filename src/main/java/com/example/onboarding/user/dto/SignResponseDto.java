package com.example.onboarding.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignResponseDto {
    private String token;

    public SignResponseDto(String token) {
        this.token = token;
    }
}
