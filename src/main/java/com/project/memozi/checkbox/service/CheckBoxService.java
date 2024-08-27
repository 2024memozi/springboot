package com.project.memozi.checkbox.service;

import com.project.memozi.checkbox.dto.CheckBoxResponseDto;
import com.project.memozi.checkbox.entity.CheckBox;
import com.project.memozi.checkbox.repository.CheckBoxRepository;
import com.project.memozi.kakao.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CheckBoxService {
    private final CheckBoxRepository checkBoxRepository;

    public CheckBoxResponseDto changeCheck(Long checkBoxId, Member member) {
        CheckBox checkBox =checkBoxRepository.findByIdAndMemoMember(checkBoxId,member)
                .orElseThrow(() -> new IllegalArgumentException("해당 체크리스트 항목이 존재하지 않습니다."));
        checkBox.doCheck();
        return new CheckBoxResponseDto(checkBox);
    }
}
