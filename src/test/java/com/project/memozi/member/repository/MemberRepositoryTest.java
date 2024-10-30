package com.project.memozi.member.repository;

import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 카카오아이디와_유저정보_조회() {
        // Given
        Member member = new Member();
        member.setKakaoId("정현진의 카카오아이디");
        member.setNickname("현지니지니");
        member.setRefreshToken("제공된 리프레시토큰");
        memberRepository.save(member);

        // When
        Optional<Member> found = memberRepository.findByKakaoId("정현진의 카카오아이디");

        // Then
        assertTrue(found.isPresent(), "카카오아이디가 일치하지 않습니다.");
        assertEquals("현지니지니", found.get().getNickname(),"닉네임이 일치하지 않습니다.");
    }
}