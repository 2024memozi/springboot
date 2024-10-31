package com.project.memozi.memo;

import com.project.memozi.category.CategoryRepository;
import com.project.memozi.category.entity.Category;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.repository.MemberRepository;
import com.project.memozi.memo.entity.Memo;
import com.project.memozi.memo.repository.MemoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
public class MemoTest {
    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Member member;
    private Category category;

    @BeforeEach
    void 회원로그인(){
        // Given
        member = new Member();
        member.setKakaoId("정현진의 카카오아이디");
        member.setNickname("현지니지니");
        member.setRefreshToken("제공된 리프레시토큰");
        member = memberRepository.save(member);

        category = new Category("투두리스트", "이미지", "#000000", member);
        category = categoryRepository.save(category);
    }

    @Test
    void 메모_작성(){
        // Given
        Memo memo = new Memo("제목", "내용", category, member);

        // When
        Memo savedMemo = memoRepository.save(memo);

        // Then
        assertNotNull(savedMemo.getId(), "메모가 저장되지 않았습니다.");
        assertEquals("제목", savedMemo.getTitle(), "메모 제목이 일치하지 않습니다.");
        assertEquals("내용", savedMemo.getContent(), "메모 내용이 일치하지 않습니다.");
    }

    @Test
    void 메모_상세조회(){
        // Given
        Memo memo = new Memo("제목", "내용", category, member);

        // When
        Memo savedMemo = memoRepository.save(memo);

        // Then
        assertNotNull(savedMemo.getId(), "메모가 저장되지 않았습니다.");
        assertEquals("제목", savedMemo.getTitle(), "메모 제목이 일치하지 않습니다.");
        assertEquals("내용", savedMemo.getContent(), "메모 내용이 일치하지 않습니다.");
    }

    @Test
    void 메모_전체조회(){
        // Given
        memoRepository.save(new Memo("첫번째 메모", "첫번째 내용", category, member));
        memoRepository.save(new Memo("두번째 메모", "두번째 내용", category, member));

        // When
        List<Memo> memos = memoRepository.findAll();

        // Then
        assertEquals(2, memos.size(), "2개의 메모가 조회되지 않았습니다.");
    }
}
