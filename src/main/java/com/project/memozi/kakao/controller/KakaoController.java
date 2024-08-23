package com.project.memozi.kakao.controller;

import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.service.CustomOAuth2UserService;
import com.project.memozi.kakao.service.CustomUserDetails;
import com.project.memozi.kakao.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<String> callback (@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        Member member = kakaoService.getUserInfo(accessToken);
        String jwtToken = kakaoService.generateJwtForUser(member);
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
    }

    @DeleteMapping("/deleteMember")
    public ResponseEntity<?> deleteMember (@AuthenticationPrincipal CustomUserDetails customUserDetails, OAuth2AuthenticationToken oAuth2AuthenticationToken){
        String accessToken = customOAuth2UserService.getAccessToken(oAuth2AuthenticationToken);
        if(accessToken != null){
            kakaoService.deleteMember(customUserDetails.getMember(), accessToken);
        }
        return ResponseEntity.ok("탈퇴하였습니다");
    }
}