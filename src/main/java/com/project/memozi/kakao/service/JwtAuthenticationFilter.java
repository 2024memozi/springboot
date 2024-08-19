package com.project.memozi.kakao.service;

import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.repository.MemberRepository;
import com.project.memozi.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            String nickname = jwtUtil.extractName(jwt);
            if (nickname != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtil.validateToken(jwt, nickname)) {
                    Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
                    if (optionalMember.isPresent()) {
                        Member member = optionalMember.get();
                        CustomUserDetails userDetails = new CustomUserDetails(member, null);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } 
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
