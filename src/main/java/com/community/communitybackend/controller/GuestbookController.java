package com.community.communitybackend.controller;

import com.community.communitybackend.dto.GuestbookRequestDto;
import com.community.communitybackend.dto.GuestbookResponseDto;
import com.community.communitybackend.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 방명록 관련 API 요청을 처리하는 컨트롤러 클래스
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/guestbook")
public class GuestbookController {

    private final GuestbookService guestbookService;

    /**
     * 방명록 전체 조회 API
     * GET /api/guestbook/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<GuestbookResponseDto>> findAllGuestbooks() {
        return ResponseEntity.ok(guestbookService.findAllGuestbooks());
    }

    /**
     * 방명록 생성 API
     * POST /api/guestbook
     * 로그인한 사용자만 사용 가능
     */
    @PostMapping
    public ResponseEntity<GuestbookResponseDto> saveGuestbook(
            @AuthenticationPrincipal User user,
            @RequestBody GuestbookRequestDto requestDto) {
        System.out.println("[컨트롤러] requestDto: " + requestDto);
        System.out.println("[컨트롤러] requestDto.getParentId(): " + requestDto.getParentId());
        String providerId = user.getUsername();
        return ResponseEntity.ok(guestbookService.saveGuestbook(providerId, requestDto));
    }

    /**
     * 방명록 수정 API
     * PUT /api/guestbook/{id}
     * 작성자만 수정 가능
     */
    @PutMapping("/{id}")
    public ResponseEntity<GuestbookResponseDto> updateGuestbook(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            @RequestBody GuestbookRequestDto requestDto) {
        String providerId = user.getUsername();
        return ResponseEntity.ok(guestbookService.updateGuestbook(id, providerId, requestDto));
    }

    /**
     * 방명록 삭제 API
     * DELETE /api/guestbook/{id}
     * 작성자만 삭제 가능
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuestbook(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        String providerId = user.getUsername();
        guestbookService.deleteGuestbook(id, providerId);
        return ResponseEntity.ok().build();
    }

    /**
     * 내가 작성한 방명록 조회 API
     * GET /api/guestbook/me
     * 로그인한 사용자만 사용 가능
     */
    @GetMapping("/me")
    public ResponseEntity<List<GuestbookResponseDto>> findMyGuestbooks(
            @AuthenticationPrincipal User user) {
        Long userId = Long.parseLong(user.getUsername());
        return ResponseEntity.ok(guestbookService.findGuestbooksByUserId(userId));
    }
} 