package com.example.onboarding.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupRequestDto {
    @Schema(description = "사용자 이름", example = "user")
    private String username;
    @Schema(description = "비밀번호", example = "password123")
    private String password;
    @Schema(description = "닉네임", example = "hello")
    private String nickname;

    public SignupRequestDto(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }
}
