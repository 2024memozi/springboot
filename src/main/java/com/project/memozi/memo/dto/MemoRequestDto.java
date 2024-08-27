package com.project.memozi.memo.dto;

import com.project.memozi.checkbox.dto.CheckBoxRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemoRequestDto {
    private String title;
    private String content;
    private List<CheckBoxRequestDto> checkBoxes;
}
