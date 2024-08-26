package com.project.memozi.diary.dto;

import com.project.memozi.diary.entity.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryResponseDto {
    private String title;
    private String content;
    private List<String> images = new ArrayList<>();

    public DiaryResponseDto(Diary diary){
        this.title = diary.getTitle();
        this.content = diary.getContent();
        this.images = diary.getImages();
    }
}
