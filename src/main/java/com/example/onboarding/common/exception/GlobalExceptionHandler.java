package com.example.onboarding.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * CustomException 처리
     * @param ex CustomException
     * @return ResponseEntity에 담긴 에러 응답
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        log.error("CustomException: {}", ex.getMessage());
        ErrorEnum error = ex.getError();
        ErrorResponse response = new ErrorResponse(error.getErrorCode(), error.getMessage());
        return new ResponseEntity<>(response, error.getStatus());
    }
}
