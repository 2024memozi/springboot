package com.project.memozi.category.repository;

import com.project.memozi.category.entity.Category;
import com.project.memozi.kakao.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByMember(Member member);
}
