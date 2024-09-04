package com.project.memozi.kakao.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.repository.MemberRepository;
import com.project.memozi.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public OAuth2SuccessHandler(JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        CustomUserDetails userDetails = (CustomUserDetails) authToken.getPrincipal();
        Member member = userDetails.getMember();

        String accessToken = jwtUtil.generateAccessToken(member.getNickname());
        String refreshToken = jwtUtil.generateRefreshToken(member.getNickname());

        member.setRefreshToken(refreshToken);
        memberRepository.save(member);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(tokens));
    }
}

