package com.project.memozi.diary.controller;

import com.project.memozi.diary.dto.DiaryRequestDto;
import com.project.memozi.diary.dto.DiaryResponseDto;
import com.project.memozi.diary.entity.Diary;
import com.project.memozi.diary.service.DiaryService;
import com.project.memozi.kakao.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<DiaryResponseDto> addDiary (@RequestPart(value = "images", required = false) List<MultipartFile> images,
                                                      @RequestParam String title,
                                                      @RequestParam String content,
                                                      @RequestParam(required = false) String location,
                                                      @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        DiaryRequestDto diaryRequestDto = new DiaryRequestDto(title,content,location);
        DiaryResponseDto diaryResponseDto = diaryService.addDiary(images,diaryRequestDto,customUserDetails.getMember());
        return ResponseEntity.ok().body(diaryResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<DiaryResponseDto>> getAllDiary(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<DiaryResponseDto> diaryResponseDto = diaryService.getDiary(customUserDetails.getMember());
        return ResponseEntity.ok().body(diaryResponseDto);
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryResponseDto> getDetailDiary(@PathVariable Long diaryId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        DiaryResponseDto diaryResponseDto = diaryService.getDetailDiary(diaryId, customUserDetails.getMember());
        return ResponseEntity.ok().body(diaryResponseDto);
    }

    @PutMapping("/{diaryId}")
    public ResponseEntity<DiaryResponseDto> updateDetailDiary(@RequestPart(value = "images", required = false) List<MultipartFile> images,
                                                              @RequestParam(required = false) String title,
                                                              @RequestParam(required = false) String content,
                                                              @RequestParam(required = false) String location,
                                                              @PathVariable Long diaryId, @AuthenticationPrincipal CustomUserDetails customUserDetails)throws IOException{
        DiaryRequestDto diaryRequestDto = new DiaryRequestDto(title,content,location);
        DiaryResponseDto diaryResponseDto = diaryService.updateDiary(images, diaryId, diaryRequestDto, customUserDetails.getMember());
        return ResponseEntity.ok().body(diaryResponseDto);
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<?> deleteDiary(@PathVariable Long diaryId, @AuthenticationPrincipal CustomUserDetails customeUserDetails){
        diaryService.deleteDiary(diaryId,customeUserDetails.getMember());
        return ResponseEntity.ok().body("다이어리가 삭제되었습니다");
    }

}
