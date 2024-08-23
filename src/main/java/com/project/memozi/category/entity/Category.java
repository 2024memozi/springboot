package com.project.memozi.category.entity;

import com.project.memozi.category.dto.CategoryRequestDto;
import com.project.memozi.color.entity.Color;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.memo.entity.Memo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @ElementCollection
    @CollectionTable(name = "category_images", joinColumns = @JoinColumn(name = "category_id"))
    @Column(name = "images_url")
    private List<String> images = new ArrayList<>();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "bgcolor_id")
    private Color bgcolor;

    @ManyToOne
    @JoinColumn(name = "txtcolor_id")
    private Color txtcolor;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List <Memo> memos;

    public Category(CategoryRequestDto categoryRequestDto, Member member) {
        this.name = categoryRequestDto.getName();
        this.member = member;
    }
}
