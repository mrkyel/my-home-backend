package com.community.communitybackend.service;

import com.community.communitybackend.domain.guestbook.Guestbook;
import com.community.communitybackend.domain.user.User;
import com.community.communitybackend.dto.GuestbookRequestDto;
import com.community.communitybackend.dto.GuestbookResponseDto;
import com.community.communitybackend.repository.GuestbookRepository;
import com.community.communitybackend.repository.UserRepository;
import com.community.communitybackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 방명록 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@RequiredArgsConstructor
@Service
public class GuestbookService {

    private final GuestbookRepository guestbookRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * 방명록 전체 조회
     * 생성일 기준 최신순으로 정렬합니다.
     */
    @Transactional(readOnly = true)
    public List<GuestbookResponseDto> findAllGuestbooks() {
        return guestbookRepository.findAllWithUserOrderByCreatedAtDesc().stream()
                .map(GuestbookResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 방명록 생성
     * 로그인한 사용자 정보와 함께 방명록을 저장합니다.
     */
    @Transactional
    public GuestbookResponseDto saveGuestbook(String providerId, GuestbookRequestDto requestDto) {
        String name = null;
        String picture = null;
        Long parentId = null;
        try {
            name = (String) requestDto.getClass().getMethod("getName").invoke(requestDto);
            picture = (String) requestDto.getClass().getMethod("getPicture").invoke(requestDto);
            parentId = (Long) requestDto.getClass().getMethod("getParentId").invoke(requestDto);
        } catch (Exception ignored) {}

        // 로그 추가
        System.out.println("[방명록] saveGuestbook 호출");
        System.out.println("  providerId: " + providerId);
        System.out.println("  name: " + name);
        System.out.println("  picture: " + picture);
        System.out.println("  parentId: " + parentId);
        System.out.println("  requestDto: " + requestDto);
        System.out.println("  requestDto.getParentId(): " + requestDto.getParentId());

        User user = userService.getOrCreateKakaoUser(providerId, name, picture);
        Guestbook parent = null;
        if (parentId != null) {
            parent = guestbookRepository.findById(parentId).orElse(null);
        }
        Guestbook guestbook = Guestbook.builder()
                .content(requestDto.getContent())
                .user(user)
                .parent(parent)
                .build();

        guestbookRepository.save(guestbook);

        // 저장 후 실제 guestbook 정보 로그
        System.out.println("  저장된 Guestbook id: " + guestbook.getId());
        System.out.println("  저장된 Guestbook user: " + (guestbook.getUser() != null ? guestbook.getUser().getId() : null));
        System.out.println("  저장된 Guestbook parent: " + (guestbook.getParent() != null ? guestbook.getParent().getId() : null));

        return new GuestbookResponseDto(guestbook);
    }

    /**
     * 방명록 수정
     * 작성자만 수정할 수 있도록 검증합니다.
     */
    @Transactional
    public GuestbookResponseDto updateGuestbook(Long id, String userProviderId, GuestbookRequestDto requestDto) {
        Guestbook guestbook = guestbookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 방명록이 존재하지 않습니다. id=" + id));

        // 작성자 검증 (providerId 기준)
        if (!guestbook.getUser().getProviderId().equals(userProviderId)) {
            throw new IllegalArgumentException("해당 방명록의 작성자가 아닙니다.");
        }

        guestbook.update(requestDto.getContent());
        return new GuestbookResponseDto(guestbook);
    }

    /**
     * 방명록 삭제
     * 작성자만 삭제할 수 있도록 검증합니다.
     */
    @Transactional
    public void deleteGuestbook(Long id, String userProviderId) {
        Guestbook guestbook = guestbookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 방명록이 존재하지 않습니다. id=" + id));

        // 작성자 검증 (providerId 기준)
        if (!guestbook.getUser().getProviderId().equals(userProviderId)) {
            throw new IllegalArgumentException("해당 방명록의 작성자가 아닙니다.");
        }

        guestbookRepository.delete(guestbook);
    }

    /**
     * 특정 사용자의 방명록 조회
     */
    @Transactional(readOnly = true)
    public List<GuestbookResponseDto> findGuestbooksByUserId(Long userId) {
        return guestbookRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(GuestbookResponseDto::new)
                .collect(Collectors.toList());
    }
} 