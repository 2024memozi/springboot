package com.project.memozi.category.entity;

import com.project.memozi.category.dto.CategoryRequestDto;
import com.project.memozi.kakao.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Category(CategoryRequestDto categoryRequestDto, Member member) {
        this.name = categoryRequestDto.getName();
        this.member = member;
    }
}
