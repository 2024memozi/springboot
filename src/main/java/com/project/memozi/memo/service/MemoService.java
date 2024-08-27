package com.project.memozi.memo.service;

import com.project.memozi.category.entity.Category;
import com.project.memozi.category.repository.CategoryRepository;
import com.project.memozi.checkbox.dto.CheckBoxRequestDto;
import com.project.memozi.checkbox.entity.CheckBox;
import com.project.memozi.checkbox.repository.CheckBoxRepository;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.memo.dto.MemoRequestDto;
import com.project.memozi.memo.dto.MemoResponseDto;
import com.project.memozi.memo.entity.Memo;
import com.project.memozi.memo.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    private final CategoryRepository categoryRepository;
    private final CheckBoxRepository checkBoxRepository;

    @Transactional
    public MemoResponseDto addMemo(Long categoryId, MemoRequestDto memoRequestDto, Member member) {
        Category category = categoryRepository.findByIdAndMember(categoryId,member)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다"));
        Memo memo = new Memo(category,memoRequestDto,member);
        memoRepository.save(memo);

        if(memoRequestDto.getCheckBoxes() != null){
            for (CheckBoxRequestDto checkBoxRequestDto : memoRequestDto.getCheckBoxes()){
                CheckBox checkBox =new CheckBox(checkBoxRequestDto.getContent(), memo);
                memo.addCheckBox(checkBox);
                checkBoxRepository.save(checkBox);
            }
        }
        return new MemoResponseDto(memo);
    }

    @Transactional (readOnly = true)
    public MemoResponseDto getMemo(Long categoryId, Long memoId, Member member) {
        Memo memo = memoRepository.findByIdAndCategoryId(memoId,categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메모가 존재하지 않습니다"));

        return new MemoResponseDto(memo);
    }

    @Transactional
    public MemoResponseDto updateMemo(Long categoryId, Long memoId, MemoRequestDto memoRequestDto, Member member){
        Memo memo = memoRepository.findByIdAndCategoryId(memoId,categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메모가 존재하지 않습니다"));

        if (!memo.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        memo.update(memoRequestDto);
        memoRepository.save(memo);
        return new MemoResponseDto(memo);
    }

    @Transactional
    public void deleteMemo(Long categoryId, Long memoId, Member member){
        Memo memo = memoRepository.findByIdAndCategoryId(memoId,categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메모가 존재하지 않습니다"));

        if (!memo.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        memoRepository.delete(memo);
    }


}
