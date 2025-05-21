package com.community.communitybackend.dto;

import com.community.communitybackend.domain.guestbook.Guestbook;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * 방명록 응답을 위한 DTO 클래스
 * 방명록 정보를 클라이언트에 반환할 때 사용합니다.
 */
@Getter
public class GuestbookResponseDto {
    
    private Long id; // 방명록 ID
    private String content; // 방명록 내용
    private String userId; // 작성자 providerId(카카오ID)
    private String userName; // 작성자 이름
    private String userPicture; // 작성자 프로필 이미지
    private LocalDateTime createdAt; // 작성 일시
    private LocalDateTime modifiedAt; // 수정 일시
    private Long parentId; // 부모 댓글 ID(대댓글용)
    
    /**
     * 엔티티를 DTO로 변환하는 생성자
     */
    public GuestbookResponseDto(Guestbook entity) {
        this.id = entity.getId();
        this.content = entity.getContent();
        this.userId = entity.getUser().getProviderId();
        this.userName = entity.getUser().getName();
        this.userPicture = entity.getUser().getPicture();
        this.createdAt = entity.getCreatedAt();
        this.modifiedAt = entity.getModifiedAt();
        this.parentId = entity.getParent() != null ? entity.getParent().getId() : null;
    }
} 