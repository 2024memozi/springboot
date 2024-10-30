package com.project.memozi.category;

import com.project.memozi.category.entity.Category;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.repository.MemberRepository;
import com.project.memozi.memo.entity.Memo;
import com.project.memozi.memo.repository.MemoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@Transactional
public class CategoryTest {
    @Autowired
    private CategoryRepository categoryRepository;
    private Member member;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemoRepository memoRepository;

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

    @Test
    void 카테고리_상세조회(){
        // Given
        Category category = new Category("정현진의 투두리스트", "첫번째 이미지", "#000000", member);
        Category savedCategory = categoryRepository.save(category);

        // When
        Optional<Category> result = categoryRepository.findByIdAndMember(savedCategory.getId(), member);

        // Then
        assertTrue(result.isPresent());
        assertEquals(savedCategory.getId(), result.get().getId(), "본인의 카테고리가 아닙니다.");
    }

    @Test
    void 검색() {
        // Given
        Category category = new Category("정현진의 투두리스트", "첫번째 이미지", "#000000", member);
        categoryRepository.save(category);

        Memo memo = new Memo("Kworld 프로젝트", "얼른끝내고 취업하고싶다.", category, member);
        memoRepository.save(memo);

        // When
        List<Category> result = categoryRepository.searchByCategoryNameOrMemoContent("얼른", member);

        // Then
        assertEquals(1, result.size(), "검색 결과의 개수가 맞지 않습니다.");
        assertTrue(result.get(0).getName().contains("현진"), "카테고리 이름이 올바르지 않습니다.");
    }

    @Test
    void 카테고리_수정(){
        // Given
        Category category = new Category("정현진의 투두리스트", "첫번째 이미지", "#000000", member);
        Category savedCategory = categoryRepository.save(category);

        // When
        savedCategory.updateName("정현진의 내일 할 일");
        savedCategory.updateRepresentImage("수정된 이미지");
        savedCategory.updateTxtColor("#ffffff");
        categoryRepository.save(savedCategory);

        // Then
        Optional<Category> result = categoryRepository.findById(savedCategory.getId());

        assertTrue(result.isPresent(), "카테고리가 존재하지 않습니다.");
        assertEquals("정현진의 내일 할 일", result.get().getName(), "카테고리 이름 수정실패");
        assertEquals("수정된 이미지", result.get().getRepresentImage(), "이미지 수정실패.");
        assertEquals("#ffffff", result.get().getTxtColor(), "텍스트 색상 수정실패");

    }

    @Test
    void 카테고리_삭제(){
        // Given
        Category category = new Category("정현진의 투두리스트", "첫번째 이미지", "#000000", member);
        Category savedCategory = categoryRepository.save(category);

        // When
        categoryRepository.delete(savedCategory);
        Optional<Category> result = categoryRepository.findById(savedCategory.getId());

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void 대량_카테고리_조회_성능_테스트() {
        // Given
        for (int i = 1; i <= 10000; i++) {
            categoryRepository.save(new Category("카테고리" + i, "이미지" + i, "#000000", member));
        }

        // Start measuring time
        long startTime = System.nanoTime();

        // When
        List<Category> categories = categoryRepository.findAllByMember(member);

        // End measuring time
        long endTime = System.nanoTime();

        // Then
        assertEquals(10000, categories.size(), "1000개의 카테고리가 조회되지 않았습니다.");

        long elapsedTime = (endTime - startTime) / 1_000_000;  // ns -> ms
        System.out.println("조회에 걸린 시간: " + elapsedTime + " ms");

        assertTrue(elapsedTime < 10000, "조회 시간이 1초를 초과했습니다.");
    }
}
