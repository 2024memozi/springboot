package com.project.memozi.category.service;

import com.project.memozi.category.dto.CategoryDetailResponseDto;
import com.project.memozi.category.dto.CategoryRequestDto;
import com.project.memozi.category.dto.CategoryResponseDto;
import com.project.memozi.category.entity.Category;
import com.project.memozi.category.repository.CategoryRepository;
import com.project.memozi.kakao.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto, Member member) {
        Category category = new Category(categoryRequestDto, member);
        categoryRepository.save(category);
        return new CategoryResponseDto(category);
    }

    @Transactional(readOnly=true)
    public CategoryDetailResponseDto getCategoryMemos(Long categoryId){
    Category category = categoryRepository.findById(categoryId)
            .orElseThrow(()->new IllegalArgumentException("해당 카테고리가 존재하지 않습니다"));
        return new CategoryDetailResponseDto(category);
    }
}
