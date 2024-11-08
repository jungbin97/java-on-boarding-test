package com.example.onboarding.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupResponseDto {
    private String username;
    private String nickname;
    private List<AuthorityResponse> authorities;

    public SignupResponseDto(String username, String nickname, List<AuthorityResponse> authorities) {
        this.username = username;
        this.nickname = nickname;
        this.authorities = authorities;
    }}
