package com.community.communitybackend.domain.guestbook;

import com.community.communitybackend.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 방명록 엔티티 클래스
 * 사용자가 남긴 방명록 메시지를 저장합니다.
 */
@Entity
@Getter
@NoArgsConstructor
public class Guestbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content; // 방명록 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 작성자 (소셜 로그인한 사용자)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Guestbook parent; // 부모 댓글(대댓글용)

    @Column(nullable = false)
    private LocalDateTime createdAt; // 작성 일시

    @Column
    private LocalDateTime modifiedAt; // 수정 일시

    @Builder
    public Guestbook(String content, User user, Guestbook parent) {
        this.content = content;
        this.user = user;
        this.parent = parent;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * 방명록 내용 업데이트 메서드
     * 기존 방명록 수정 시 호출됩니다.
     */
    public void update(String content) {
        this.content = content;
        this.modifiedAt = LocalDateTime.now();
    }
} 