package com.project.memozi.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDto {
    private String name;
    private String defaultImageUrl;
    private Long bgColor;
    private Long txtColor;
}
