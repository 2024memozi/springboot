package com.project.memozi.checkbox.controller;

import com.project.memozi.checkbox.dto.CheckBoxResponseDto;
import com.project.memozi.checkbox.service.CheckBoxService;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.kakao.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CheckBoxController {
    private final CheckBoxService checkBoxService;

    @PutMapping("/{checkboxId}/check")
    public ResponseEntity<CheckBoxResponseDto> changeCheck (@PathVariable Long checkboxId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        Member member = customUserDetails.getMember();
        CheckBoxResponseDto checkBoxResponseDto =checkBoxService.changeCheck(checkboxId,member);
        return ResponseEntity.ok(checkBoxResponseDto);
    }
}
