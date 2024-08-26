package com.project.memozi.category.service;

import com.project.memozi.category.dto.CategoryDetailResponseDto;
import com.project.memozi.category.dto.CategoryRequestDto;
import com.project.memozi.category.dto.CategoryResponseDto;
import com.project.memozi.category.entity.Category;
import com.project.memozi.category.repository.CategoryRepository;
import com.project.memozi.color.entity.Color;
import com.project.memozi.color.entity.Type;
import com.project.memozi.color.repository.ColorRepository;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public CategoryResponseDto addCategory(MultipartFile image, CategoryRequestDto categoryRequestDto, Member member)throws IOException {

        Color bgColor = null;
        if(categoryRequestDto.getBgColor() != null) {
            bgColor = colorRepository.findByIdAndType(categoryRequestDto.getBgColor(), Type.BACKGROUND)
                    .orElseThrow(()->new IllegalArgumentException("해당 배경색이 존재하지 않습니다."));
        }

        Color txtColor = colorRepository.findByIdAndType(categoryRequestDto.getTxtColor(), Type.TEXT)
                .orElseThrow(() -> new IllegalArgumentException("해당 텍스트 색상이 존재하지 않습니다."));

        String representImageUrl;

        if (image != null && !image.isEmpty()) {
            representImageUrl = s3Uploader.upload(image, "category");
        }  else if (categoryRequestDto.getDefaultImageUrl() != null) {
            representImageUrl = categoryRequestDto.getDefaultImageUrl();
        } else {
            throw new IllegalArgumentException("이미지를 선택해주세요.");
        }
        Category category = new Category(categoryRequestDto.getName(),representImageUrl, bgColor, txtColor, member);

        if (!category.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

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
