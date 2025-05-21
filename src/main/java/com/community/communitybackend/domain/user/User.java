package com.community.communitybackend.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 사용자 정보를 저장하는 엔티티 클래스
 * 소셜 로그인(카카오, 네이버 등)으로 인증된 사용자 정보를 저장합니다.
 */
@Entity
@Table(name = "users") // 'user'는 예약어이므로 'users'로 테이블명 지정
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 사용자 이름(닉네임)

    @Column(nullable = false)
    private String email; // 이메일

    @Column
    private String picture; // 프로필 이미지 URL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 사용자 권한

    @Column(nullable = false)
    private String provider; // 소셜 로그인 제공자 (kakao, naver, google 등)

    @Column(nullable = false)
    private String providerId; // 소셜 로그인 제공자의 사용자 고유 ID

    @Column(nullable = false)
    private LocalDateTime createdAt; // 가입 일시

    @Column
    private LocalDateTime lastLoginAt; // 마지막 로그인 일시

    @Builder
    public User(String name, String email, String picture, Role role, 
               String provider, String providerId) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createdAt = LocalDateTime.now();
        this.lastLoginAt = LocalDateTime.now();
    }

    /**
     * 사용자 정보 업데이트 메서드
     * 소셜 로그인 시 사용자 정보가 변경되었을 때 호출됩니다.
     */
    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    /**
     * 로그인 시간 업데이트 메서드
     */
    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }
} 