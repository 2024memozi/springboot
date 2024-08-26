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

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories(Member member) {

        List<Category>categories = categoryRepository.findAllByMember(member);
        List<CategoryResponseDto>categoryResponseDtos = categories.stream()
                .map(CategoryResponseDto::new)
                .collect(Collectors.toList());

        if(!categories.isEmpty()){
            Category defaultCategory = categories.get(0);
            CategoryDetailResponseDto defaultCategoryMemo = new CategoryDetailResponseDto(defaultCategory);
            categoryResponseDtos.get(0).setMemo(defaultCategoryMemo.getMemos());
        }

        return categoryResponseDtos;
    }

    @Transactional(readOnly=true)
    public CategoryDetailResponseDto getCategoryMemos(Long categoryId,Member member){
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(()->new IllegalArgumentException("해당 카테고리가 존재하지 않습니다"));

        if (!category.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        return new CategoryDetailResponseDto(category);
    }

    @Transactional
    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto categoryRequestDto, Member member){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new IllegalArgumentException("해당 카테고리가 존재하지 않습니다"));

        if (!category.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        return new CategoryResponseDto(category);
    }

    @Transactional
    public void deleteCategory(Long categoryId, Member member){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new IllegalArgumentException("해당 카테고리가 존재하지 않습니다"));

        if (!category.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        categoryRepository.delete(category);
    }
}
