package com.example.onboarding.user.service;

import com.example.onboarding.user.User;
import com.example.onboarding.user.UserRole;
import com.example.onboarding.user.dto.AuthorityResponse;
import com.example.onboarding.user.dto.SignupRequestDto;
import com.example.onboarding.user.dto.SignupResponseDto;
import com.example.onboarding.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResponseDto signup(SignupRequestDto request) {
        String username = request.getUsername();
        String password = passwordEncoder.encode(request.getPassword());

        Optional<User> checkUsername = userRepository.findByUsername(username);
        // 회원 중복확인
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        User user = new User(username, password, request.getNickname(), UserRole.USER);
        User savedUser = userRepository.save(user);

        List<AuthorityResponse> authorities = List.of(new AuthorityResponse(savedUser.getUserRole().getAuthority()));

        // dto 변환
        return new SignupResponseDto(savedUser.getUsername(), savedUser.getNickname(), authorities);
    }
}
