package com.project.memozi.memo.repository;

import com.project.memozi.kakao.entity.Member;
import com.project.memozi.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo,Long> {
    Optional<Memo> findByIdAndCategoryId(Long memoId, Long categoryId);
    Optional<Memo> findByIdAndMember(Long memoId, Member member);
}
