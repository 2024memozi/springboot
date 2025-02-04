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

    public Memo(String title, String content, Category category, Member member) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.member = member;
    }

//    public void update(MemoRequestDto memoRequestDto){
//        if (memoRequestDto.getTitle() != null) {
//            this.title = memoRequestDto.getTitle();
//        }
//        if (memoRequestDto.getContent() != null) {
//            this.content = memoRequestDto.getContent();
//        }
//    }

    public void updateTitle(String title) {
        if (title != null) {
            this.title = title;
        }
    }

    public void updateContent(String content) {
        if (content != null) {
            this.content = content;
        }
    }

    public void addCheckBox(CheckBox checkBox) {
        this.checkBoxes.add(checkBox);
        checkBox.setMemo(this);
    }

}
