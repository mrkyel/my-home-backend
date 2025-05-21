package com.community.communitybackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 방명록 작성/수정 요청을 위한 DTO 클래스
 * 클라이언트로부터 방명록 내용을 전달받습니다.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class GuestbookRequestDto {
    
    private String content; // 방명록 내용
    private String name; // 작성자 이름(닉네임)
    private String picture; // 작성자 프로필 이미지
    @JsonProperty("parentId")
    private Long parentId; // 부모 댓글 ID(대댓글용)
    
    @Builder
    public GuestbookRequestDto(String content, String name, String picture, Long parentId) {
        this.content = content;
        this.name = name;
        this.picture = picture;
        this.parentId = parentId;
    }
} 