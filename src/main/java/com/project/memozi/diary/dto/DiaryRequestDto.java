package com.project.memozi.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryRequestDto {
    private String title;
    private String content;
    private String location;
}
