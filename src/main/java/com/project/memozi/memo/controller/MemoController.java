package com.project.memozi.memo.controller;

import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.service.CustomUserDetails;
import com.project.memozi.memo.dto.MemoRequestDto;
import com.project.memozi.memo.dto.MemoResponseDto;
import com.project.memozi.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memo")
public class MemoController {
    private final MemoService memoService;

    @PostMapping("/{categoryId}")
    public ResponseEntity<MemoResponseDto> addMemo (@PathVariable Long categoryId, @RequestBody MemoRequestDto memoRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Member member = customUserDetails.getMember();
        MemoResponseDto memoResponseDto = memoService.addMemo(categoryId,memoRequestDto,member);
        return ResponseEntity.ok(memoResponseDto);
    }

    @GetMapping("/{categoryId}/{memoId}")
    public ResponseEntity<MemoResponseDto>getMemo (@PathVariable Long categoryId, @PathVariable Long memoId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Member member = customUserDetails.getMember();
        MemoResponseDto memoResponseDto = memoService.getMemo(categoryId,memoId,member);
        return ResponseEntity.ok(memoResponseDto);
    }

    @PutMapping("/{categoryId}/{memoId}")
    public ResponseEntity<MemoResponseDto> updateMemo (@PathVariable Long categoryId, @PathVariable Long memoId, @RequestBody MemoRequestDto memoRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Member member = customUserDetails.getMember();
        MemoResponseDto memoResponseDto = memoService.updateMemo(categoryId, memoId, memoRequestDto, member);
        return ResponseEntity.ok(memoResponseDto);
    }

    @DeleteMapping("/{categoryId}/{memoId}")
    public ResponseEntity<?> deleteMemo (@PathVariable Long categoryId, @PathVariable Long memoId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Member member = customUserDetails.getMember();
        memoService.deleteMemo(categoryId,memoId,member);
        return ResponseEntity.ok("메모가 삭제 되었습니다");
    }

}
