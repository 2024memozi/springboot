package com.project.memozi.category.controller;

import com.project.memozi.category.dto.CategoryDetailResponseDto;
import com.project.memozi.category.dto.CategoryRequestDto;
import com.project.memozi.category.dto.CategoryResponseDto;
import com.project.memozi.category.dto.CategorySearchResponseDto;
import com.project.memozi.category.service.CategoryService;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> addCategory(@RequestPart(value = "images", required = false) MultipartFile image,
                                         @RequestParam String name,
                                         @RequestParam(required = false) String defaultImageUrl,
                                         @RequestParam(required = false) Long bgColorId,
                                         @RequestParam Long txtColorId,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails)throws IOException {
        Member member = customUserDetails.getMember();
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(name,defaultImageUrl,bgColorId,txtColorId);
        CategoryResponseDto categoryResponseDto = categoryService.addCategory(image, categoryRequestDto, member);
        return ResponseEntity.ok(categoryResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategory(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable){
        Member member = customUserDetails.getMember();
        List<CategoryResponseDto> categoryList = categoryService.getAllCategories(member,pageable);
        return ResponseEntity.ok(categoryList);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDetailResponseDto>getCategoryMemo(@PathVariable Long categoryId,@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable){
        Member member = customUserDetails.getMember();
        CategoryDetailResponseDto categoryDetailResponseDto = categoryService.getCategoryMemos(categoryId,member,pageable);
        return ResponseEntity.ok(categoryDetailResponseDto);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto>updateCategory(@RequestPart(value = "images", required = false) MultipartFile image,
                                                             @PathVariable Long categoryId,
                                                             @RequestParam String name,
                                                             @RequestParam(required = false) String defaultImageUrl,
                                                             @RequestParam(required = false) Long bgColorId,
                                                             @RequestParam(required = false) Long txtColorId,
                                                             @AuthenticationPrincipal CustomUserDetails customUserDetails)throws IOException{
        Member member = customUserDetails.getMember();
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(name, defaultImageUrl, bgColorId, txtColorId);
        CategoryResponseDto categoryResponseDto = categoryService.updateCategory(image, categoryId, categoryRequestDto, member);
        return ResponseEntity.ok(categoryResponseDto);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory (@PathVariable Long categoryId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Member member = customUserDetails.getMember();
        categoryService.deleteCategory(categoryId,member);
        return ResponseEntity.ok("카테고리가 삭제되었습니다");
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategorySearchResponseDto>> search (@RequestParam String query, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Member member = customUserDetails.getMember();
        List<CategorySearchResponseDto> results = categoryService.search(query,member);
        return ResponseEntity.ok(results);
    }
}
