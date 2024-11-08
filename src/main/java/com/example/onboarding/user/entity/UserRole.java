package com.example.onboarding.user.entity;

/**
 * UserRole
 * 사용자 권한을 정의한 열거형 클래스입니다.
 * 사용자 권한은 사용자와 관리자로 구분됩니다.
 */
public enum UserRole {
    USER(Authority.USER),  // 사용자 권한
    ADMIN(Authority.ADMIN);  // 관리자 권한

    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
