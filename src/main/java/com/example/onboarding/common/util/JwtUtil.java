package com.example.onboarding.common.util;

import com.example.onboarding.common.exception.CustomException;
import com.example.onboarding.user.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.example.onboarding.common.exception.ErrorEnum.*;

/**
 * JwtUtil
 * JWT 토큰을 생성하고 검증하는 유틸리티 클래스입니다.
 */
@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // Refresh Token 식별자
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.token.expiration.access}")
    private Long accessTokenExpiration;
    @Value("${jwt.token.expiration.refresh}")
    private Long refreshTokenExpiration;
    private Key key;
    private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    protected void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * 토큰 생성
     *
     * @param username 사용자 이름
     * @param userRole 사용자 권한
     * @return 생성된 토큰
     */
    public String createToken(String username, UserRole userRole) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, userRole.getAuthority())
                        .setExpiration(new Date(date.getTime() + accessTokenExpiration))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    /**
     * Refresh Token 생성
     *
     * @param username 사용자 이름
     * @param userRole 사용자 권한
     * @return 생성된 Refresh Token
     */
    public String createRefreshToken(String username, UserRole userRole) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .claim(AUTHORIZATION_KEY, userRole.getAuthority())
                        .setExpiration(new Date(date.getTime() + refreshTokenExpiration))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    /**
     * bearer 접두어 제거
     * @param token: 토큰
     * @return: 접두어 제거된 토큰
     */
    public String substringToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        throw new IllegalArgumentException("Token Not Found");
    }

    /**
     * 토큰에서 사용자 정보 추출
     *
     * @param token 토큰
     * @return 사용자 정보
     */
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토큰 유효성 검사
     *
     * @param token 토큰
     * @return 유효성 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new CustomException(TOKEN_EXPIRATION);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(UNSUPPORTED_TOKEN);
        } catch (MalformedJwtException e) {
            throw new CustomException(INVALID_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new CustomException(INVALID_TOKEN);
        }
    }

    /**
     * Access Token 추출
     *
     * @param req 요청
     * @return Access Token
     */
    public String getTokenFromRequest(HttpServletRequest req) {
        return req.getHeader(AUTHORIZATION_HEADER);
    }

    /**
     * Refresh Token 추출
     *
     * @param req 요청
     * @return Refresh Token
     */
    public String getRefreshTokenFromRequest(HttpServletRequest req) {
        return req.getHeader(REFRESH_TOKEN_HEADER);
    }
}