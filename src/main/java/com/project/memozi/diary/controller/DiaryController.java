package com.project.memozi.diary.controller;

import com.project.memozi.diary.dto.DiaryRequestDto;
import com.project.memozi.diary.dto.DiaryResponseDto;
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
                                                      @AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
        DiaryRequestDto diaryRequestDto = new DiaryRequestDto(title,content);
        DiaryResponseDto diaryResponseDto = diaryService.addDiary(images,diaryRequestDto,customUserDetails.getMember());
        return ResponseEntity.ok().body(diaryResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<DiaryResponseDto>> getAllDiary(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<DiaryResponseDto> diaryResponseDto = diaryService.getDiary(customUserDetails.getMember());
        return ResponseEntity.ok().body(diaryResponseDto);
    }

}
