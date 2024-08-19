package com.project.memozi.category.controller;

import com.project.memozi.category.dto.CategoryRequestDto;
import com.project.memozi.category.dto.CategoryResponseDto;
import com.project.memozi.category.service.CategoryService;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequestDto categoryRequestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Member member = customUserDetails.getMember();
        CategoryResponseDto categoryResponseDto = categoryService.addCategory(categoryRequestDto, member);
        return ResponseEntity.ok(categoryResponseDto);
    }
}
