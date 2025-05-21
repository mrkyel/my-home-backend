package com.community.communitybackend.repository;

import com.community.communitybackend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * 사용자 정보에 접근하기 위한 레포지토리 인터페이스
 * JpaRepository를 상속받아 기본적인 CRUD 기능을 제공합니다.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일로 사용자 조회
     * 소셜 로그인 시 이메일을 기준으로 사용자를 식별합니다.
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 소셜 로그인 제공자와 제공자 ID로 사용자 조회
     * 소셜 로그인 시 사용자를 식별하는 데 사용됩니다.
     */
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
} 