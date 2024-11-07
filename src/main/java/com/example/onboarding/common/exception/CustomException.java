package com.example.onboarding.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorEnum error;

    public CustomException(ErrorEnum error) {
        super(error.getMessage());
        this.error = error;
    }
}
