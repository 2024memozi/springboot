package com.project.memozi.kakao.controller;

import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.service.CustomUserDetails;
import com.project.memozi.kakao.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class KakaoController {
    private final KakaoService kakaoService;

    @PostMapping("/oauth2/kakao/login")
    public ResponseEntity<Map<String, String>> handleKakaoLogin (@RequestBody Map<String, String> body){
        String accessToken = body.get("accessToken");
        if(accessToken == null){
            return new ResponseEntity<>(Map.of("error", "AccessToken이 필요합니다."), HttpStatus.BAD_REQUEST);
        }

        Member member = kakaoService.getUserInfo(accessToken);
        Map<String, String> tokens = kakaoService.generateJwtForUser(member);

        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @DeleteMapping("/deleteMember")
    public ResponseEntity<?> deleteMember (@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody Map<String, String> body){

        String accessToken = body.get("accessToken");
        if (accessToken == null || accessToken.isEmpty()) {
            return new ResponseEntity<>("카카오 Access Token이 필요합니다.", HttpStatus.BAD_REQUEST);
        }
        kakaoService.deleteMember(customUserDetails.getMember(), accessToken);
        return ResponseEntity.ok("탈퇴하였습니다");
    }
}