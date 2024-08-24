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

import java.util.List;


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

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategory(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        Member member = customUserDetails.getMember();
        List<CategoryResponseDto> categoryList = categoryService.getAllCategories(member);
        return ResponseEntity.ok(categoryList);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDetailResponseDto>getCategoryMemo(@PathVariable Long categoryId,@AuthenticationPrincipal CustomUserDetails customUserDetails){
        Member member = customUserDetails.getMember();
        CategoryDetailResponseDto categoryDetailResponseDto = categoryService.getCategoryMemos(categoryId,member);
        return ResponseEntity.ok(categoryDetailResponseDto);
    }
}
