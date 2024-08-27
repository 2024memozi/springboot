package com.project.memozi.memo.entity;

import com.project.memozi.category.entity.Category;
import com.project.memozi.checkbox.entity.CheckBox;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.memo.dto.MemoRequestDto;
import com.project.memozi.util.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Memo extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content; 

    @Column(nullable = false)
    private String dayOfWeek;

    @OneToMany(mappedBy = "memo", cascade = CascadeType.ALL)
    private List<CheckBox> checkBoxes = new ArrayList<>();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @PrePersist
    @PreUpdate
    private void setDay(){
        LocalDate dateTime = getUpdatedAt() != null ? getUpdatedAt() : getCreatedAt();
        this.dayOfWeek = dateTime.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.KOREAN);
    }

    public Memo(Category category, MemoRequestDto memoRequestDto, Member member){
        this.title = memoRequestDto.getTitle();
        this.content = memoRequestDto.getContent();
        this.category = category;
        this.member = member;
    }

    public void update(MemoRequestDto memoRequestDto){
        this.title = memoRequestDto.getTitle();
        this.content = memoRequestDto.getContent();
    }

}
