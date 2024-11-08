package com.example.onboarding.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AuthorityResponse {
    @Schema(
            title = "권한 이름",
            description = "사용자의 권한 이름입니다.",
            defaultValue = "ROLE_USER"
    )
    private String authorityName;

    public AuthorityResponse(String authorityName) {
        this.authorityName = authorityName;
    }
}