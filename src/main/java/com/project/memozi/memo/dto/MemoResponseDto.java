package com.project.memozi.memo.dto;

import com.project.memozi.category.entity.Category;
import com.project.memozi.checkbox.dto.CheckBoxResponseDto;
import com.project.memozi.memo.entity.Memo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemoResponseDto {
    private Long memoId;
    private String title;
    private String content;
    private String dayOfWeek;
    private List<CheckBoxResponseDto> checkBoxes;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public MemoResponseDto(Memo memo){
        this.memoId = memo.getId();
        this.title = memo.getTitle();
        this.content = memo.getContent();
        this.dayOfWeek = memo.getDayOfWeek();
        this.createdAt = memo.getCreatedAt();
        this.updatedAt = memo.getUpdatedAt();
        this.checkBoxes =memo.getCheckBoxes().stream()
                .map(CheckBoxResponseDto::new)
                .collect(Collectors.toList());
    }
}
