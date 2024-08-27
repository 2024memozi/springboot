package com.project.memozi.checkbox.repository;

import com.project.memozi.checkbox.entity.CheckBox;
import com.project.memozi.kakao.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckBoxRepository extends JpaRepository<CheckBox,Long> {
    Optional<CheckBox>findByIdAndMemoMember(Long checkboxId, Member member);
}
