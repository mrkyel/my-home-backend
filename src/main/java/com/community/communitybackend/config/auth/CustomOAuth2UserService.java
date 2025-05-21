package com.community.communitybackend.config.auth;

import com.community.communitybackend.domain.user.Role;
import com.community.communitybackend.domain.user.User;
import com.community.communitybackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * OAuth2 로그인 후 사용자 정보를 처리하는 서비스
 * DefaultOAuth2UserService를 상속받아 인증 이후 사용자 정보 저장 등의 추가 작업을 수행합니다.
 */
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    
    private final UserRepository userRepository;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        
        // 현재 로그인 진행 중인 서비스를 구분하는 코드 (kakao, naver, google 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        
        // OAuth2 로그인 진행 시 키가 되는 필드값 (PK와 같은 의미)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        
        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.of(
                registrationId, userNameAttributeName, oAuth2User.getAttributes());
        
        // 사용자 정보 저장 또는 업데이트
        User user = saveOrUpdate(attributes, registrationId);
        
        // 마지막 로그인 시간 업데이트
        user.updateLastLoginAt();
        userRepository.save(user);
        
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }
    
    /**
     * 사용자 정보 저장 또는 업데이트
     * 소셜 로그인 시 이미 가입된 사용자면 정보를 업데이트하고, 아니면 신규 가입 처리합니다.
     */
    private User saveOrUpdate(OAuthAttributes attributes, String provider) {
        Optional<User> userOptional = userRepository.findByProviderAndProviderId(
                provider, attributes.getProviderId());
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.update(attributes.getName(), attributes.getPicture());
        }
        
        User user = attributes.toEntity(provider);
        return userRepository.save(user);
    }
} 