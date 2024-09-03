package com.project.memozi.category.repository;

import com.project.memozi.category.entity.Category;
import com.project.memozi.kakao.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByMember(Member member);
    Optional<Category> findByIdAndMember(Long categoryId, Member member);

    @Query("SELECT DISTINCT c FROM Category c JOIN c.memos m WHERE c.member = :member AND (c.name LIKE %:query% OR m.content LIKE %:query%)")
    List<Category> searchByCategoryNameOrMemoContent(@Param("query") String query, @Param("member") Member member);

}
