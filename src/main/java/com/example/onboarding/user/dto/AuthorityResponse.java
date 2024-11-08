package com.example.onboarding.user.dto;

import lombok.Getter;

@Getter
public class AuthorityResponse {
    private String authorityName;

    public AuthorityResponse(String authorityName) {
        this.authorityName = authorityName;
    }
}