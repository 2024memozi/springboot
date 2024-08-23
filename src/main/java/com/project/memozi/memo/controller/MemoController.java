package com.project.memozi.memo.controller;

import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.service.CustomUserDetails;
import com.project.memozi.memo.dto.MemoRequestDto;
import com.project.memozi.memo.dto.MemoResponseDto;
import com.project.memozi.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemoController {
    private final MemoService memoService;

    @PostMapping("{categoryId}/memo")
    public ResponseEntity<MemoResponseDto> postMemo(@PathVariable Long categoryId, @RequestBody MemoRequestDto memoRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Member member = customUserDetails.getMember();
        MemoResponseDto memoResponseDto = memoService.addMemo(categoryId,memoRequestDto,member);
        return ResponseEntity.ok(memoResponseDto);
    }

}
