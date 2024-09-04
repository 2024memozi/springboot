package com.project.memozi.kakao.controller;

import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.repository.MemberRepository;
import com.project.memozi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class TokenController {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");

        if (refreshToken == null || refreshToken.isEmpty()) {
            return new ResponseEntity<>(Map.of("error", "Refresh Token이 필요합니다."), HttpStatus.BAD_REQUEST);
        }

        String nickname = jwtUtil.extractName(refreshToken);
        if (nickname == null || jwtUtil.isTokenExpired(refreshToken)) {
            return new ResponseEntity<>(Map.of("error", "유효하지 않은 Refresh Token입니다."), HttpStatus.UNAUTHORIZED);
        }

        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!refreshToken.equals(member.getRefreshToken())) {
            return new ResponseEntity<>(Map.of("error", "Refresh Token이 일치하지 않습니다."), HttpStatus.UNAUTHORIZED);
        }

        String newAccessToken = jwtUtil.generateAccessToken(nickname);
        return new ResponseEntity<>(Map.of("accessToken", newAccessToken), HttpStatus.OK);
    }
}
