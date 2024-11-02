package com.project.memozi.diary;

import com.project.memozi.diary.dto.DiaryRequestDto;
import com.project.memozi.diary.dto.DiaryResponseDto;
import com.project.memozi.diary.entity.Diary;
import com.project.memozi.diary.repository.DiaryRepository;
import com.project.memozi.diary.service.DiaryService;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.repository.MemberRepository;
import com.project.memozi.s3.S3Uploader;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class DiaryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private DiaryRepository diaryRepository;

    @MockBean
    private S3Uploader s3Uploader;

    private Member member;

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
    void 다이어리_작성() throws IOException{
        // Given
        String title = "취준";
        String content = "취업하고 싶다";
        String location = "집";
        List<MultipartFile> images = new ArrayList<>();
        DiaryRequestDto diaryRequestDto = new DiaryRequestDto(title, content, location);

        String imageUrl = "https://취업취업취업취업.jpg";
        when(s3Uploader.upload(any(MultipartFile.class))).thenReturn(imageUrl);

        // When
        DiaryResponseDto diaryResponseDto = diaryService.addDiary(images, diaryRequestDto, member);

        // Then
        assertNotNull(diaryResponseDto);
        assertEquals(title, diaryResponseDto.getTitle());
        assertEquals(content, diaryResponseDto.getContent());
        assertEquals(location, diaryResponseDto.getLocation());

        List<Diary> savedDiaries = diaryRepository.findAll();
        assertEquals(1, savedDiaries.size());
        Diary savedDiary = savedDiaries.get(0);
        assertEquals(title, savedDiary.getTitle());
        assertEquals(content, savedDiary.getContent());
        assertEquals(location, savedDiary.getLocation());
    }

    @Test
    void 다이어리_전체_조회() {
        Diary diary1 = new Diary("첫번째 다이어리", "내용1", null, new ArrayList<>(), "월요일", member);
        Diary diary2 = new Diary("두번째 다이어리", "내용2", null, new ArrayList<>(), "화요일", member);

        diaryRepository.save(diary1);
        diaryRepository.save(diary2);

        List<Diary> diaries = diaryRepository.findAll();

        assertEquals(2, diaries.size(), "Error.");
    }

    @Test
    void 다이어리_상세_조회() {
        Diary diary = new Diary("첫번째 다이어리", "내용1", null, new ArrayList<>(), "월요일", member);
        Diary savedDiary = diaryRepository.save(diary);

        Optional<Diary> foundDiary = diaryRepository.findById(savedDiary.getId());

        assertTrue(foundDiary.isPresent(), "다이어리가 존재하지 않습니다.");
        assertEquals(savedDiary.getId(), foundDiary.get().getId(), "다이어리 ID가 일치하지 않습니다.");
        assertEquals("첫번째 다이어리", foundDiary.get().getTitle(), "다이어리 제목이 일치하지 않습니다.");
        assertEquals("내용1", foundDiary.get().getContent(), "다이어리 내용이 일치하지 않습니다.");
    }

    @Test
    void 다이어리_수정() throws IOException {
        Diary diary = new Diary("취업", "취업하고싶다", "집", new ArrayList<>(), "월요일", member);
        Diary savedDiary = diaryRepository.save(diary);

        DiaryRequestDto updateRequest = new DiaryRequestDto("공부중", "스프링부트", "카페");
        List<MultipartFile> images = new ArrayList<>();
        when(s3Uploader.upload(any(MultipartFile.class))).thenReturn("https://수정된이미지.jpg");

        DiaryResponseDto updatedDiary = diaryService.updateDiary(images, savedDiary.getId(), updateRequest, member);

        assertNotNull(updatedDiary, "수정에 실패하였습니다.");
        assertEquals(savedDiary.getId(), updatedDiary.getDiaryId(), "다이어리가 존재하지 않습니다.");
        assertEquals("공부중", updatedDiary.getTitle());
        assertEquals("스프링부트", updatedDiary.getContent());
        assertEquals("카페", updatedDiary.getLocation());
    }

    @Test
    void 다이어리_삭제() {
        // Given
        Diary diary = new Diary("취업", "취업하고싶다", "집", new ArrayList<>(), "월요일", member);
        Diary savedDiary = diaryRepository.save(diary);

        // When
        diaryService.deleteDiary(savedDiary.getId(), member);

        // Then
        Optional<Diary> foundDiary = diaryRepository.findById(savedDiary.getId());
        assertTrue(foundDiary.isEmpty(), "삭제에 실패하였습니다.");
    }
}
