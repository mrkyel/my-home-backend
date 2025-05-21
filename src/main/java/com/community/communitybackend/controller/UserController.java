package com.community.communitybackend.controller;

import com.community.communitybackend.dto.UserResponseDto;
import com.community.communitybackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 관련 API 요청을 처리하는 컨트롤러 클래스
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    /**
     * 현재 로그인한 사용자 정보 조회 API
     * GET /api/user/me
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return ResponseEntity.status(401).build(); // 인증 안 됨
        }
        
        Long userId = Long.parseLong(oAuth2User.getAttribute("id").toString());
        return ResponseEntity.ok(userService.findUserById(userId));
    }
} 