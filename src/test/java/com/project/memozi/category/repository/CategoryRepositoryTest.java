package com.project.memozi.category.repository;

import com.project.memozi.category.entity.Category;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Transactional
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;
    private Member member;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void 회원로그인(){
        // Given
        member = new Member();
        member.setKakaoId("정현진의 카카오아이디");
        member.setNickname("현지니지니");
        member.setRefreshToken("제공된 리프레시토큰");
        member = memberRepository.save(member);
    }

    @Test
    void 멤버별_카테고리_전체조회(){
        // Given
        Category category1 = new Category("투두리스트", "첫번째 이미지", "#000000", member);
        categoryRepository.save(category1);

        Category category2 = new Category("오늘 할 일", "두번째 이미지", "#ffffff", member);
        categoryRepository.save(category2);

        // When
        List<Category> categories = categoryRepository.findAllByMember(member);

        // Then
        assertEquals(2, categories.size());
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("투두리스트")));
        assertTrue(categories.stream().anyMatch(c -> c.getName().equals("오늘 할 일")));
    }
}
