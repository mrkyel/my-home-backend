package com.community.communitybackend.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 사용자 권한 정의 열거형
 * 스프링 시큐리티에서 사용할 사용자의 권한 레벨을 정의합니다.
 */
@Getter
@RequiredArgsConstructor
public enum Role {
    
    // 일반 사용자: 기본 권한
    USER("ROLE_USER", "일반 사용자"),
    
    // 관리자: 모든 권한
    ADMIN("ROLE_ADMIN", "관리자");
    
    private final String key;
    private final String title;
} 