package com.example.onboarding.user.service;

import com.example.onboarding.common.exception.CustomException;
import com.example.onboarding.common.exception.ErrorEnum;
import com.example.onboarding.user.entity.User;
import com.example.onboarding.user.entity.UserRole;
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
            throw new CustomException(ErrorEnum.INVALID_INPUT_VALUE);
        }

        User user = new User(username, password, request.getNickname(), UserRole.USER);
        User savedUser = userRepository.save(user);

        List<AuthorityResponse> authorities = List.of(new AuthorityResponse(savedUser.getUserRole().getAuthority()));

        // dto 변환
        return new SignupResponseDto(savedUser.getUsername(), savedUser.getNickname(), authorities);
    }
}
