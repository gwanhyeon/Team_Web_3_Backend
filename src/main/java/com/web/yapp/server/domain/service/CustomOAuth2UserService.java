package com.web.yapp.server.domain.service;

import com.web.yapp.server.controller.dto.OAuthAttributesDto;
import com.web.yapp.server.controller.dto.SessionUserDto;
import com.web.yapp.server.domain.User;
import com.web.yapp.server.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final HttpSession httpSession;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.
                getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributesDto attributes = OAuthAttributesDto.of(registrationId, userNameAttributeName,
                oAuth2User.getAttributes());
        User user = saveOrupdate(attributes);
        /* 유저 테이블 저장 */
        httpSession.setAttribute("user", new SessionUserDto(user));
        httpSession.setAttribute("accessToken", userRequest.getAccessToken().getTokenValue());
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }
    private User saveOrupdate(OAuthAttributesDto attributes){
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(),
                        attributes.getProfile_url())
                )
                .orElse(attributes.toEntity());
        return userRepository.save(user);
    }


}
