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
    private Long categoryId;
    private String name;
    private String representImage;
//    private String bgColor;
    private String txtColor;
    private List<MemoResponseDto> memos;
    private boolean hasNext;

    public CategoryDetailResponseDto(Category category) {
        this.categoryId = category.getId();
        this.name = category.getName();
        this.representImage = category.getRepresentImage();
//        this.bgColor = category.getBgColor() != null ? category.getBgColor().getCode() : null;
//        this.txtColor = category.getTxtColor() != null ? category.getTxtColor().getCode() : null;
        this.txtColor = category.getTxtColor();
        this.memos = category.getMemos().stream()
                .map(MemoResponseDto::new)
                .collect(Collectors.toList());
    }

    public CategoryDetailResponseDto(Category category, List<MemoResponseDto> memos, boolean hasNext) {
        this.categoryId = category.getId();
        this.name = category.getName();
        this.representImage = category.getRepresentImage();
//        this.bgColor = category.getBgColor() != null ? category.getBgColor().getCode() : null;
//        this.txtColor = category.getTxtColor() != null ? category.getTxtColor().getCode() : null;
        this.txtColor = category.getTxtColor();
        this.memos = memos;
        this.hasNext = hasNext;
    }
}
