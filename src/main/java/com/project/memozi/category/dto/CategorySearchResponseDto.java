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

    public CategorySearchResponseDto(Category category,List<MemoResponseDto> filteredMemos){
        this.name = category.getName();
        this.count = filteredMemos.size();
        this.memos = filteredMemos;
    }

    public void setMemoCount(int count){
        this.count = count;
    }
}
