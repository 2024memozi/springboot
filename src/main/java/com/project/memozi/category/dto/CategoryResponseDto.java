package com.project.memozi.category.dto;

import com.project.memozi.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto {
    private String name;

    public CategoryResponseDto(Category category) {
        this.name = category.getName();
    }
}
