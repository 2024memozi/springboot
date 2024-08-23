package com.project.memozi.kakao.service;

import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.repository.MemberRepository;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final OAuth2AuthorizedClientService oauth2AuthorizedClientService;

    public CustomOAuth2UserService(MemberRepository memberRepository, OAuth2AuthorizedClientService oauth2AuthorizedClientService) {
        this.memberRepository = memberRepository;
        this.oauth2AuthorizedClientService = oauth2AuthorizedClientService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String kakaoId = String.valueOf(oAuth2User.getAttributes().get("id"));
        Map<String, Object> properties = (Map<String, Object>) oAuth2User.getAttributes().get("properties");
        String nickname = (String) properties.get("nickname");

        Member member = memberRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> {
                    Member newMember = new Member();
                    newMember.setKakaoId(kakaoId);
                    newMember.setNickname(nickname);
                    return memberRepository.save(newMember);
                });

        return new CustomUserDetails(member, oAuth2User.getAttributes());
    }

    public String getAccessToken(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = oauth2AuthorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName());

        return client.getAccessToken().getTokenValue();
    }
}
