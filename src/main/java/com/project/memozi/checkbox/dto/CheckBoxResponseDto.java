package com.project.memozi.checkbox.dto;

import com.project.memozi.checkbox.entity.CheckBox;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckBoxResponseDto {
    private Long id;
    private String content;
    private boolean isChecked;

    public CheckBoxResponseDto(CheckBox checkBox){
        this.id = checkBox.getId();
        this.content = checkBox.getContent();
        this.isChecked = checkBox.isChecked();
    }
}
