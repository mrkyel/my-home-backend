package com.community.communitybackend.config.auth;

import com.community.communitybackend.domain.user.Role;
import com.community.communitybackend.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import java.util.Map;

/**
 * OAuth2 인증 과정에서 접근권한을 통해 가져온 사용자 정보를 담는 클래스
 * 여러 소셜 로그인 제공자(카카오, 네이버, 구글 등)의 응답을 처리합니다.
 */
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes; // OAuth2 반환하는 유저 정보 Map
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private String providerId; // 소셜 로그인 제공자의 사용자 고유 ID
    
    @Builder
    public OAuthAttributes(Map<String, Object> attributes, 
                         String nameAttributeKey, String name, 
                         String email, String picture, String providerId) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.providerId = providerId;
    }
    
    /**
     * OAuth2 제공자별로 유저 정보를 변환하는 메서드
     */
    public static OAuthAttributes of(String registrationId, 
                                    String userNameAttributeName, 
                                    Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        } else if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        
        // 기본은 구글 처리
        return ofGoogle(userNameAttributeName, attributes);
    }
    
    /**
     * 구글 로그인 정보 처리
     */
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .providerId(attributes.get(userNameAttributeName).toString())
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    
    /**
     * 네이버 로그인 정보 처리
     */
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        
        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .providerId(response.get("id").toString())
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    
    /**
     * 카카오 로그인 정보 처리
     */
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        
        return OAuthAttributes.builder()
                .name((String) properties.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .picture((String) properties.get("profile_image"))
                .providerId(attributes.get(userNameAttributeName).toString())
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    
    /**
     * User 엔티티를 생성하는 메서드
     * 처음 가입시 USER 권한을 부여합니다.
     */
    public User toEntity(String provider) {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER) // 기본 권한은 USER
                .provider(provider)
                .providerId(providerId)
                .build();
    }
} 