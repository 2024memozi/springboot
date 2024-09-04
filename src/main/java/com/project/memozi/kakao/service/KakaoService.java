package com.project.memozi.kakao.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.repository.MemberRepository;
import com.project.memozi.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class KakaoService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.scope}")
    private String scope;

    @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
    private String authorizationUri;

    public String getKakaoAuthUrl() {
        // 필요한 파라미터를 URL로 인코딩하여 카카오 인증 URL 생성
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        return authorizationUri + "?client_id=" + clientId
                + "&redirect_uri=" + encodedRedirectUri
                + "&response_type=code"
                + "&scope=" + scope;
    }

//    public String getAccessTokenFromKakao(String code) {
//        String url = "https://kauth.kakao.com/oauth/token";
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        String body = "grant_type=authorization_code" +
//                "&client_id=" + clientId +
//                "&redirect_uri=" + redirectUri +
//                "&code=" + code;
//        HttpEntity<String> request = new HttpEntity<>(body, headers);
//        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
//        try {
//            String responseBody = response.getBody();
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonNode = objectMapper.readTree(responseBody);
//            String accessToken = jsonNode.get("access_token").asText();
//            return accessToken;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("에러 발생", e);
//        }
//    }

    public Member getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        try {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            String kakaoId = jsonNode.get("id").asText();
            String nickname = jsonNode.get("properties").get("nickname").asText();
            Optional<Member> memberOptional = memberRepository.findByKakaoId(kakaoId);
            return memberOptional.orElseGet(() -> {
                Member newMember = new Member();
                newMember.setKakaoId(kakaoId);
                newMember.setNickname(nickname);
                return memberRepository.save(newMember);
            });
        } catch (Exception e) {
            throw new RuntimeException("에러발생", e);
        }
    }

    public String generateJwtForUser(Member member) {
        return jwtUtil.generateToken(member.getNickname());
    }

    @Transactional
    public void deleteMember(Member member,String accessToken){
        unlinkKakao(accessToken);
        memberRepository.delete(member);
    }

    public void unlinkKakao(String accessToken) {
        String url = "https://kapi.kakao.com/v1/user/unlink";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(httpHeaders);

        try {
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            throw new RuntimeException("카카오 연결 해제 실패", e);
        }
    }
}
