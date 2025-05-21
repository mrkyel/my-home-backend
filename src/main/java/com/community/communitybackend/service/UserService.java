package com.community.communitybackend.service;

import com.community.communitybackend.domain.user.User;
import com.community.communitybackend.dto.UserResponseDto;
import com.community.communitybackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.community.communitybackend.domain.user.Role;
import java.time.LocalDateTime;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 정보 조회
     * 현재 로그인한 사용자의 정보를 조회합니다.
     */
    @Transactional(readOnly = true)
    public UserResponseDto findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. id=" + id));
        
        return new UserResponseDto(user);
    }

    /**
     * provider, providerId로 유저를 조회하고 없으면 자동 생성 (카카오 전용)
     */
    @Transactional
    public User getOrCreateKakaoUser(String kakaoUserId, String name, String picture) {
        return userRepository.findByProviderAndProviderId("kakao", kakaoUserId)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .name(name != null ? name : ("카카오유저_" + kakaoUserId))
                            .email(kakaoUserId + "@kakao.com")
                            .picture(picture)
                            .role(Role.USER)
                            .provider("kakao")
                            .providerId(kakaoUserId)
                            .build();
                    return userRepository.save(newUser);
                });
    }
} 