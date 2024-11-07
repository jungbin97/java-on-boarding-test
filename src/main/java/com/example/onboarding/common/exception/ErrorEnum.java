package com.example.onboarding.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorEnum {
    TOKEN_EXPIRATION(UNAUTHORIZED, "AUTH_001", "토큰이 만료되었습니다."),
    UNSUPPORTED_TOKEN(UNAUTHORIZED, "AUTH_002", "지원하지 않는 토큰입니다."),
    INVALID_TOKEN(BAD_REQUEST, "AUTH_003", "잘못된 토큰 입니다.");

    private final HttpStatus status; // HTTP 상태 코드
    private final String errorCode; // 내부 시스템에서 사용하는 에러 코드
    private final String message; // 사용자에게 보여줄 에러 메시지
}
