package com.project.memozi.diary.dto;

import com.project.memozi.diary.entity.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryResponseDto {
    private Long diaryId;
    private String title;
    private String content;
    private String location;
    private List<String> images = new ArrayList<>();
    private String dayOfWeek;
    private LocalDate createdAt;

    public DiaryResponseDto(Diary diary){
        this.diaryId = diary.getId();
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.location = diary.getLocation();
        this.images = diary.getImages();
        this.dayOfWeek = diary.getDayOfWeek();
        this.createdAt = diary.getCreatedAt();
    }
}
