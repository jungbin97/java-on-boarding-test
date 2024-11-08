package com.example.onboarding.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupResponseDto {
    @Schema(
            title = "사용자 이름",
            description = "사용자 이름입니다.",
            defaultValue = "user"
    )
    private String username;
    @Schema(
            title = "닉네임",
            description = "사용자의 닉네임입니다.",
            defaultValue = "hello"
    )
    private String nickname;

    private List<AuthorityResponse> authorities;

    public SignupResponseDto(String username, String nickname, List<AuthorityResponse> authorities) {
        this.username = username;
        this.nickname = nickname;
        this.authorities = authorities;
    }}
