package com.project.memozi.category.dto;

import com.project.memozi.category.entity.Category;
import com.project.memozi.memo.dto.MemoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto {
    private String name;
    private String representImage;
//    private String bgColor;
    private String txtColor;
    private List<MemoResponseDto>memo = new ArrayList<>();

    public CategoryResponseDto(Category category) {
        this.name = category.getName();
        this.representImage = category.getRepresentImage();
//        this.bgColor = category.getBgColor() != null ? category.getBgColor().getCode() : null;
        this.txtColor = category.getTxtColor() != null ? category.getTxtColor().getCode() : null;
    }

    public void setMemo(List<MemoResponseDto> memo){
        this.memo = memo;
    }
}
