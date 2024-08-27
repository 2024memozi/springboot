package com.project.memozi.checkbox.service;

import com.project.memozi.checkbox.dto.CheckBoxRequestDto;
import com.project.memozi.checkbox.dto.CheckBoxResponseDto;
import com.project.memozi.checkbox.entity.CheckBox;
import com.project.memozi.checkbox.repository.CheckBoxRepository;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.memo.entity.Memo;
import com.project.memozi.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckBoxService {
    private final MemoRepository memoRepository;
    private final CheckBoxRepository checkBoxRepository;

    public CheckBoxResponseDto changeCheck(Long checkBoxId, Member member) {
        CheckBox checkBox =checkBoxRepository.findByIdAndMemoMember(checkBoxId,member)
                .orElseThrow(() -> new IllegalArgumentException("해당 체크리스트 항목이 존재하지 않습니다."));

        checkBox.doCheck();
        checkBoxRepository.save(checkBox);
        return new CheckBoxResponseDto(checkBox);
    }

    public CheckBoxResponseDto addCheckBox(Long memoId, CheckBoxRequestDto checkBoxRequestDto, Member member) {
        Memo memo = memoRepository.findByIdAndMember(memoId, member)
                .orElseThrow(() -> new IllegalArgumentException("해당 메모가 존재하지 않습니다."));

        CheckBox checkBox =new CheckBox(checkBoxRequestDto.getContent(), memo);
        memo.addCheckBox(checkBox);
        checkBoxRepository.save(checkBox);
        return new CheckBoxResponseDto(checkBox);
    }
}
