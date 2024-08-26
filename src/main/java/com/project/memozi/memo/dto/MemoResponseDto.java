package com.project.memozi.memo.dto;

import com.project.memozi.category.entity.Category;
import com.project.memozi.memo.entity.Memo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemoResponseDto {
    private String title;
    private String content;
    private String dayOfWeek;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public MemoResponseDto(Memo memo){
        this.title = memo.getTitle();
        this.content = memo.getContent();
        this.dayOfWeek = memo.getDayOfWeek();
        this.createdAt = memo.getCreatedAt();
        this.updatedAt = memo.getUpdatedAt();
    }
}
