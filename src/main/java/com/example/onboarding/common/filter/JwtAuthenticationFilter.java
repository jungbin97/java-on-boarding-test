package com.example.onboarding.common.filter;

import com.example.onboarding.common.exception.CustomException;
import com.example.onboarding.common.exception.ErrorEnum;
import com.example.onboarding.common.security.UserDetailsImpl;
import com.example.onboarding.common.util.JwtUtil;
import com.example.onboarding.user.UserRole;
import com.example.onboarding.user.dto.SignRequestDto;
import com.example.onboarding.user.dto.SignResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


/**
 * 로그인 시도 시 JWT 토큰을 생성하는 필터입니다. (로그인 인증 필터)
 * UsernamePasswordAuthenticationFilter를 상속받아 로그인 시도 시 JWT 토큰을 생성합니다.
 * 로그인 성공 시 JWT 토큰을 생성하여 응답합니다.
 * 로그인 실패 시
 */
@Slf4j(topic = "JwtAuthenticationFilter")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/sign"); // 로그인 엔드 포인트
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            SignRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), SignRequestDto.class);

            // 사용자 인증 정보를 생성하여 반환
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorEnum.INVALID_INPUT_VALUE);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRole role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getUserRole();

        // JWT 토큰 생성
        String token = jwtUtil.createToken(username, role);

        // SignResponseDto를 생성하여 JSON 응답으로 반환
        SignResponseDto responseDto = new SignResponseDto(token);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseDto));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401);
    }

}
