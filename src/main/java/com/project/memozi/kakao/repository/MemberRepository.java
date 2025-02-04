package com.project.memozi.kakao.repository;

import com.project.memozi.kakao.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member>findByKakaoId(String kakaoId);
    Optional<Member>findByNickname(String nickname);
}
