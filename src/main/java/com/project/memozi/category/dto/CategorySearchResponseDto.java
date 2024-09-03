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
public class CategorySearchResponseDto {
    private String name;
    private int count;
    private List<MemoResponseDto> memos;

    public CategorySearchResponseDto(Category category){
        this.name = category.getName();
        this.count = category.getMemos().size();
        this.memos = category.getMemos().stream()
                .map(MemoResponseDto::new)
                .collect(Collectors.toList());
    }

    public void setMemoCount(int count){
        this.count = count;
    }
}
