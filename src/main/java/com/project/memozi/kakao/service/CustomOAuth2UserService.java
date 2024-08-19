package com.project.memozi.kakao.service;

import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String accessToken = userRequest.getAccessToken().getTokenValue();
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
}
