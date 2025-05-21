package com.community.communitybackend.dto;

import com.community.communitybackend.domain.user.User;
import lombok.Getter;

/**
 * 사용자 정보 응답을 위한 DTO 클래스
 * 로그인한 사용자 정보를 클라이언트에 반환할 때 사용합니다.
 */
@Getter
public class UserResponseDto {
    
    private Long id; // 사용자 ID
    private String name; // 사용자 이름
    private String email; // 이메일
    private String picture; // 프로필 이미지 URL
    
    /**
     * 엔티티를 DTO로 변환하는 생성자
     */
    public UserResponseDto(User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.picture = entity.getPicture();
    }
} 