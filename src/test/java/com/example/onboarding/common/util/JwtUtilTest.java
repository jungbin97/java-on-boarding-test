package com.example.onboarding.common.util;

import com.example.onboarding.common.exception.CustomException;
import com.example.onboarding.user.UserRole;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.*;

class JwtUtilTest {
    private JwtUtil jwtUtil;


    @BeforeEach
    public void init() throws Exception {
        jwtUtil = new JwtUtil();

        // 리플랙션 기법을 사용하여 private 필드에 값을 주입합니다.
        setPrivateField(jwtUtil, "secretKey", "65SU7L2U65SpIO2VtOuzuOyCrOuejCDrsJTrs7Qg66mN7Lap7J20");
        setPrivateField(jwtUtil, "accessTokenExpiration", 3600000L); // 1시간
        setPrivateField(jwtUtil, "refreshTokenExpiration", 7200000L); // 2시간
        jwtUtil.init();

    }

    private void setPrivateField(Object target, String fieldName,  Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    @DisplayName("액세스 토큰 생성 테스트")
    void testCreateToken() {
        // given
        String username = "user";
        UserRole userRole = UserRole.USER;

        // when
        String token = jwtUtil.createToken(username, userRole);

        // then
        assertThat(token).contains(JwtUtil.BEARER_PREFIX);
    }

    @Test
    @DisplayName("토큰에서 사용자 정보 추출 테스트")
    void testGetUserInfoFromToken() {
        // given
        String username = "user";
        UserRole userRole = UserRole.USER;
        String token = jwtUtil.createToken(username, userRole);

        // when
        Claims resultUsername = jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(token));

        // then
        assertThat(resultUsername.getSubject()).isEqualTo(username);
        assertThat(resultUsername.get(JwtUtil.AUTHORIZATION_KEY)).isEqualTo(userRole.getAuthority());
    }

    @Test
    @DisplayName("토큰 검증 테스트 - 성공")
    void testValidToken_ValidToken() {
        // given
        String username = "user";
        UserRole userRole = UserRole.USER;
        String token = jwtUtil.createToken(username, userRole);

        // when
        boolean isValid = jwtUtil.validateToken(jwtUtil.substringToken(token));

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("토큰 검증 테스트 - 만료된 토큰")
    void testValidToken_ExpiredToken() throws Exception {
        // given
        String username = "user";
        UserRole userRole = UserRole.USER;
        setPrivateField(jwtUtil, "accessTokenExpiration", 0L); // 0초
        String token = jwtUtil.createToken(username, userRole);

        // when & then
        assertThatThrownBy(() -> jwtUtil.validateToken(jwtUtil.substringToken(token)))
                .isInstanceOf(CustomException.class)
                .hasMessage("토큰이 만료되었습니다.");
    }

    @Test
    @DisplayName("토큰 검증 테스트 - 유효하지 않은 토큰")
    void testValidToken_InvalidToken() {
        // given
        String token = "sdsf";

        // when & then
        assertThatThrownBy(() -> jwtUtil.validateToken(token))
                .isInstanceOf(CustomException.class)
                .hasMessage("잘못된 토큰 입니다.");
    }
}