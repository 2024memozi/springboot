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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void 다이어리_전체조회() {
        Diary diary1 = new Diary(null, "첫번째 다이어리", "내용1", null, new ArrayList<>(), null, member);
        Diary diary2 = new Diary(null, "두번째 다이어리", "내용2", null, new ArrayList<>(), null, member);

        diaryRepository.save(diary1);
        diaryRepository.save(diary2);

        List<Diary> diaries = diaryRepository.findAll();

        assertEquals(2, diaries.size(), "2개의 다이어리가 조회되지 않았습니다.");
    }
}
