package com.project.memozi.category.dto;

import com.project.memozi.category.entity.Category;
import com.project.memozi.memo.dto.MemoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDetailResponseDto {
    private String name;
    private String representImage;
    private String bgColor;
    private String txtColor;
    private List<MemoResponseDto> memos;

    public CategoryDetailResponseDto(Category category) {
        this.name = category.getName();
        this.representImage = category.getRepresentImage();
        this.bgColor = category.getBgColor() != null ? category.getBgColor().getCode() : null;
        this.txtColor = category.getTxtColor() != null ? category.getTxtColor().getCode() : null;
        this.memos = category.getMemos().stream()
                .map(MemoResponseDto::new)
                .collect(Collectors.toList());
    }
}
