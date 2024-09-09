package com.project.memozi.category.service;

import com.project.memozi.category.dto.CategoryDetailResponseDto;
import com.project.memozi.category.dto.CategoryRequestDto;
import com.project.memozi.category.dto.CategoryResponseDto;
import com.project.memozi.category.dto.CategorySearchResponseDto;
import com.project.memozi.category.entity.Category;
import com.project.memozi.category.repository.CategoryRepository;
import com.project.memozi.color.entity.Color;
import com.project.memozi.color.entity.Type;
import com.project.memozi.color.repository.ColorRepository;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.memo.dto.MemoResponseDto;
import com.project.memozi.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public CategoryResponseDto addCategory(MultipartFile image, CategoryRequestDto categoryRequestDto, Member member) throws IOException {

        boolean isBgColorSelected = categoryRequestDto.getBgColorImageUrl() != null;
        boolean isDefaultImageSelected = categoryRequestDto.getDefaultImageUrl() != null;
        boolean isImageUploaded = image != null && !image.isEmpty();

        if ((isBgColorSelected && isDefaultImageSelected) ||
                (isBgColorSelected && isImageUploaded) ||
                (isDefaultImageSelected && isImageUploaded)) {
            throw new IllegalArgumentException("배경색, 기본 이미지, 갤러리는 동시에 선택할 수 없습니다.");
        }

//        Color bgColor = null;
//        if(isBgColorSelected) {
//            bgColor = colorRepository.findByIdAndType(categoryRequestDto.getBgColor(), Type.BACKGROUND)
//                    .orElseThrow(()->new IllegalArgumentException("해당 배경색이 존재하지 않습니다."));
//        }

        Color txtColor = colorRepository.findByIdAndType(categoryRequestDto.getTxtColor(), Type.TEXT)
                .orElseThrow(() -> new IllegalArgumentException("해당 텍스트 색상이 존재하지 않습니다."));

        String representImageUrl;

        if (isImageUploaded) {
            representImageUrl = s3Uploader.upload(image);
        } else if (isDefaultImageSelected) {
            representImageUrl = categoryRequestDto.getDefaultImageUrl();
        } else if (isBgColorSelected) {
            representImageUrl = categoryRequestDto.getBgColorImageUrl();
        }
        else {
            throw new IllegalArgumentException("이미지를 선택해주세요.");
        }

        Category category = new Category(categoryRequestDto.getName(),representImageUrl, txtColor, member);

        if (!category.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        categoryRepository.save(category);
        return new CategoryResponseDto(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories(Member member, Pageable pageable) {
        List<Category> categories = categoryRepository.findAllByMember(member);
        List<CategoryResponseDto> categoryResponseDtos = new ArrayList<>();

        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            CategoryResponseDto categoryResponseDto = new CategoryResponseDto(category);

            if(i == 0){
                List<MemoResponseDto> pagedMemos = category.getMemos().stream()
                        .skip((long) pageable.getPageNumber()*pageable.getPageSize())
                        .limit(pageable.getPageSize())
                        .map(MemoResponseDto::new)
                        .collect(Collectors.toList());

                boolean hasNext = category.getMemos().size() > (pageable.getPageNumber() + 1) * pageable.getPageSize();

                CategoryDetailResponseDto firstCategoryMemo = new CategoryDetailResponseDto(category, pagedMemos, hasNext);
                categoryResponseDto.setMemo(firstCategoryMemo.getMemos());
            }
            categoryResponseDtos.add(categoryResponseDto);
        }
        return categoryResponseDtos;
    }

    @Transactional(readOnly=true)
    public CategoryDetailResponseDto getCategoryMemos(Long categoryId,Member member,Pageable pageable){
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(()->new IllegalArgumentException("해당 카테고리가 존재하지 않습니다"));

        if (!category.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        List<MemoResponseDto> memos = category.getMemos().stream()
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .map(MemoResponseDto::new)
                .collect(Collectors.toList());

        boolean hasNext = category.getMemos().size() > (pageable.getPageNumber() + 1) * pageable.getPageSize();

        return new CategoryDetailResponseDto(category,memos,hasNext);
    }

    @Transactional
    public CategoryResponseDto updateCategory(MultipartFile image, Long categoryId, CategoryRequestDto categoryRequestDto, Member member) throws IOException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다"));

        if (!category.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        boolean isDefaultImageSelected = categoryRequestDto.getDefaultImageUrl() != null;
        boolean isImageUploaded = image != null && !image.isEmpty();
        boolean isBgColorSelected = categoryRequestDto.getBgColorImageUrl() != null;

        if ((isBgColorSelected && isDefaultImageSelected) ||
                (isBgColorSelected && isImageUploaded) ||
                (isDefaultImageSelected && isImageUploaded)) {
            throw new IllegalArgumentException("배경색, 기본 이미지, 갤러리는 동시에 선택할 수 없습니다.");
        }

//        if (isBgColorSelected) {
//            Color bgColor = colorRepository.findByIdAndType(categoryRequestDto.getBgColor(), Type.BACKGROUND)
//                    .orElseThrow(() -> new IllegalArgumentException("해당 배경색이 존재하지 않습니다."));
//            category.updateBgColor(bgColor);
//        }
        if (isBgColorSelected) {
            category.updateRepresentImage(categoryRequestDto.getBgColorImageUrl());
        }

        if (categoryRequestDto.getTxtColor() != null) {
            Color txtColor = colorRepository.findByIdAndType(categoryRequestDto.getTxtColor(), Type.TEXT)
                    .orElseThrow(() -> new IllegalArgumentException("해당 텍스트 색상이 존재하지 않습니다."));
            category.updateTxtColor(txtColor);
        }

        if (categoryRequestDto.getName() != null && !categoryRequestDto.getName().isEmpty()) {
            category.updateName(categoryRequestDto.getName());
        }

        String oldImageUrl = category.getRepresentImage();

        try {
            if (isImageUploaded) {
                if (oldImageUrl != null) {
                    String oldFileName = extractFileNameFromUrl(oldImageUrl);
                    s3Uploader.deleteFile(oldFileName);
                }
                String newImage = s3Uploader.upload(image);
                category.updateRepresentImage(newImage);
            } else if (isDefaultImageSelected) {
                if (oldImageUrl != null && !oldImageUrl.equals(categoryRequestDto.getDefaultImageUrl())) {
                    String oldFileName = extractFileNameFromUrl(oldImageUrl);
                    s3Uploader.deleteFile(oldFileName);
                }
                category.updateRepresentImage(categoryRequestDto.getDefaultImageUrl());
            }
        }catch(IOException e){
                throw new RuntimeException("파일 처리 중 오류가 발생하였스빈다." + e.getMessage(),e);
            }

        Category updateCategory = categoryRepository.save(category);

        return new CategoryResponseDto(updateCategory);
    }


    private String extractFileNameFromUrl(String imageUrl) {
        try{
            return "uploads/" + imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        }catch(Exception e){
            throw new IllegalArgumentException("파일 이름을 추출하는 중 오류가 발생하였습니다.");
        }
    }

    @Transactional
    public void deleteCategory(Long categoryId, Member member){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다"));

        if (!category.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        String imageUrl = category.getRepresentImage();
        categoryRepository.delete(category);

        if (imageUrl != null) {
            String filePath = extractFileNameFromUrl(imageUrl);

            try {
                s3Uploader.deleteFile(filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional(readOnly = true)
    public List<CategorySearchResponseDto> search (String query, Member member){
        List<Category> categories = categoryRepository.searchByCategoryNameOrMemoContent(query, member);
        return categories.stream().map(category->{
            CategorySearchResponseDto categorySearchResponseDto = new CategorySearchResponseDto(category);
            categorySearchResponseDto.setMemoCount(category.getMemos().size());
            return categorySearchResponseDto;
        }).collect(Collectors.toList());
    }
}
