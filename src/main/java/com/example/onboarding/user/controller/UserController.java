package com.example.onboarding.user.controller;

import com.example.onboarding.user.dto.SignupRequestDto;
import com.example.onboarding.user.dto.SignupResponseDto;
import com.example.onboarding.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 회원가입
     * @param request 회원가입 요청 정보
     *               - username: 사용자 이름 <br>
     *               - password: 비밀번호 <br>
     *               - nickname: 닉네임 <br>
     * @return 회원가입 응답 정보
     *              - username: 사용자 이름 <br>
     *              - nickname: 닉네임 <br>
     *              - authorities: 권한 목록 <br>
     */
    @PostMapping("/signup")
    public SignupResponseDto signup(@RequestBody SignupRequestDto request) {
        return userService.signup(request);
    }
}
