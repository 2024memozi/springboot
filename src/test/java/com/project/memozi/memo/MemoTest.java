package com.project.memozi.memo;

import com.project.memozi.category.CategoryRepository;
import com.project.memozi.category.entity.Category;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.repository.MemberRepository;
import com.project.memozi.memo.dto.MemoRequestDto;
import com.project.memozi.memo.dto.MemoResponseDto;
import com.project.memozi.memo.entity.Memo;
import com.project.memozi.memo.repository.MemoRepository;
import com.project.memozi.memo.service.MemoService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
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

    @MockBean
    private MemoService memoService;

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

    @Test
    void 대량_메모_조회_성능_테스트(){
        // Given
        for (int i = 1; i <= 1000; i++) {
            memoRepository.save(new Memo("제목" + i, "내용" + i, category, member));
        }

        long startTime = System.nanoTime();

        // When
        List<Memo> memos = memoRepository.findAll();

        long endTime = System.nanoTime();

        // Then
        assertEquals(1000, memos.size(), "1000개의 메모가 조회되지 않았습니다.");

        long elapsedTime = (endTime - startTime) / 1_000_000;
        System.out.println("조회에 걸린 시간: " + elapsedTime + " ms");

        assertTrue(elapsedTime < 1000, "조회 시간이 1초를 초과했습니다.");
    }

//    @Test
//    void 메모_수정() {
//        // Given
//        Memo memo = new Memo("제목", "내용", category, member);
//        Memo savedMemo = memoRepository.save(memo);
//        MemoRequestDto memoRequestDto = new MemoRequestDto("수정된 제목", "수정된 내용", null);
//
//        // 정확한 값 사용
//        when(memoRepository.findByIdAndCategoryId(savedMemo.getId(), category.getId()))
//                .thenReturn(Optional.of(memo));
//
//        // When
//        MemoResponseDto result = memoService.updateMemo(category.getId(), savedMemo.getId(), memoRequestDto, member);
//
//        // Then
//        assertNotNull(result);
//        assertEquals("수정된 제목", result.getTitle());
//        assertEquals("수정된 내용", result.getContent());
//    }
//
//    @Test
//    void 메모_삭제() {
//        // Given
//        Memo memo = new Memo("제목", "내용", category, member);
//        Memo savedMemo = memoRepository.save(memo);
//
//        when(memoRepository.findByIdAndCategoryId(savedMemo.getId(), category.getId()))
//                .thenReturn(Optional.of(memo));
//
//        // When
//        memoService.deleteMemo(category.getId(), savedMemo.getId(), member);
//
//        // Then
//        verify(memoRepository, times(1)).delete(memo);
//    }
}