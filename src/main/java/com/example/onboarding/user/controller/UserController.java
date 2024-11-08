package com.example.onboarding.user.controller;

import com.example.onboarding.common.exception.ErrorResponse;
import com.example.onboarding.user.dto.SignupRequestDto;
import com.example.onboarding.user.dto.SignupResponseDto;
import com.example.onboarding.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
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
    @Operation(
            summary = "회원가입",
            description = "사용자로부터 회원가입 정보를 받아 새로운 계정을 생성합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공 응답"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/signup")
    public SignupResponseDto signup(@org.springframework.web.bind.annotation.RequestBody SignupRequestDto request) {
        return userService.signup(request);
    }
}
