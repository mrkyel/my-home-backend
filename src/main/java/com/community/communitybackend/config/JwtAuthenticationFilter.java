package com.community.communitybackend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private boolean validateKakaoAccessToken(String accessToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v1/user/access_token_info",
                HttpMethod.GET,
                entity,
                String.class
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            System.out.println("[JWT 필터] 카카오 access_token 검증 실패: " + e.getMessage());
            return false;
        }
    }

    private String getKakaoUserId(String accessToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                String.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                return root.path("id").asText();
            }
        } catch (Exception e) {
            System.out.println("[JWT 필터] 카카오 사용자 정보 조회 실패: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("[JWT 필터] doFilterInternal 진입");
        String bearerToken = request.getHeader("Authorization");
        System.out.println("[JWT 필터] Authorization 헤더: " + bearerToken);
        String jwt = null;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            jwt = bearerToken.substring(7);
        }
        System.out.println("[JWT 필터] 추출된 jwt: " + jwt);

        try {
            if (jwt != null) {
                // 카카오 access_token 검증
                if (validateKakaoAccessToken(jwt)) {
                    String userId = getKakaoUserId(jwt);
                    if (userId != null) {
                        User principal = new User(userId, "", Collections.emptyList());
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println("[JWT 필터] 카카오 인증 성공, userId: " + userId);
                    } else {
                        System.out.println("[JWT 필터] 카카오 사용자 정보 조회 실패");
                    }
                } else {
                    System.out.println("[JWT 필터] 카카오 access_token 검증 실패");
                }
            }
        } catch (Exception e) {
            System.out.println("[JWT 필터] 토큰 파싱 실패: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
} 