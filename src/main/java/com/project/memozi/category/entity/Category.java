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

    @Column(nullable = false)
    private String representImage;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

//    @ManyToOne
//    @JoinColumn(name = "bgcolor_id")
//    private Color bgColor;

//    @ManyToOne
//    @JoinColumn(name = "txtcolor_id")
//    private Color txtColor;

    @Column(nullable = false)
    private String txtColor;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List <Memo> memos;

    public Category(String name, String representImage, String txtColor, Member member) {
        this.name = name;
        this.representImage = representImage;
//        this.bgColor = bgColor;
        this.txtColor = txtColor;
        this.member = member;
    }

    public void updateName (String name){
        this.name = name;
    }
//    public void updateBgColor (Color bgColor){
//        this.bgColor = bgColor;
//    }

    public void updateTxtColor(String txtColor){
        this.txtColor = txtColor;
    }

    public void updateRepresentImage(String representImage){
        this.representImage = representImage;
    }
}
