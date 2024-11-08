package com.example.onboarding.common.filter;

import com.example.onboarding.common.exception.CustomException;
import com.example.onboarding.common.exception.ErrorEnum;
import com.example.onboarding.common.security.UserDetailsServiceImpl;
import com.example.onboarding.common.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 토큰을 검증하고 인증하는 필터입니다. (인가 필터)
 * OncePerRequestFilter를 상속받아 JWT 토큰을 검증하고 인증합니다.
 * JWT 토큰이 유효하면 인증 객체를 생성하여 SecurityContext에 저장합니다.
 * JWT 토큰이 유효하지 않으면 예외를 발생시킵니다.
 */
@Slf4j(topic = "JwtAuthorizationFilter")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = jwtUtil.getTokenFromRequest(request);

        if (StringUtils.hasText(tokenValue)) {
            // JWT 토큰 substring
            tokenValue = jwtUtil.substringToken(tokenValue);
            log.info(tokenValue);
            try {
                if (!jwtUtil.validateToken(tokenValue)) {
                    throw new CustomException(ErrorEnum.INVALID_TOKEN);
                }
                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
                String username = info.getSubject();
                setAuthentication(username);
            } catch (CustomException e) {
                handleException(response, "validate : ", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } catch (Exception e) {
                handleException(response, "Authentication Error: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private void handleException(HttpServletResponse res, String message, int statusCode) throws IOException {
        res.setStatus(statusCode);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(new ObjectMapper().writeValueAsString(message));
    }
}
