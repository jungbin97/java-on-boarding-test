package com.example.onboarding.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String errorCode; // 내부 에러 코드
    private final String message;   // 사용자 메시지
}