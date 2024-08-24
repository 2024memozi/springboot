package com.project.memozi.category.controller;

import com.project.memozi.category.dto.CategoryDetailResponseDto;
import com.project.memozi.category.dto.CategoryRequestDto;
import com.project.memozi.category.dto.CategoryResponseDto;
import com.project.memozi.category.service.CategoryService;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequestDto categoryRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Member member = customUserDetails.getMember();
        CategoryResponseDto categoryResponseDto = categoryService.addCategory(categoryRequestDto, member);
        return ResponseEntity.ok(categoryResponseDto);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDetailResponseDto>getCategoryMemo(@PathVariable Long categoryId){
        CategoryDetailResponseDto categoryDetailResponseDto = categoryService.getCategoryMemos(categoryId);
        return ResponseEntity.ok(categoryDetailResponseDto);
    }
}
