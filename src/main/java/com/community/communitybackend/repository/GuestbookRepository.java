package com.community.communitybackend.repository;

import com.community.communitybackend.domain.guestbook.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

/**
 * 방명록 정보에 접근하기 위한 레포지토리 인터페이스
 * JpaRepository를 상속받아 기본적인 CRUD 기능을 제공합니다.
 */
public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {

    /**
     * 모든 방명록을 생성일 기준 내림차순(최신순)으로 조회
     * JPQL 쿼리를 사용하여 방명록 작성자 정보를 함께 로딩(N+1 문제 방지)
     */
    @Query("SELECT g FROM Guestbook g JOIN FETCH g.user ORDER BY g.createdAt DESC")
    List<Guestbook> findAllWithUserOrderByCreatedAtDesc();
    
    /**
     * 특정 사용자가 작성한 방명록 조회
     */
    List<Guestbook> findByUserIdOrderByCreatedAtDesc(Long userId);
} 