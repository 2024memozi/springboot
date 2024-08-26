package com.project.memozi.diary.entity;

import com.project.memozi.diary.dto.DiaryRequestDto;
import com.project.memozi.kakao.entity.Member;
import com.project.memozi.util.TimeStamped;
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
public class Diary extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ElementCollection
    @CollectionTable(name = "diary_images", joinColumns = @JoinColumn(name = "diary_id"))
    @Column(name = "images_url")
    private List<String> images = new ArrayList<>();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Diary(DiaryRequestDto diaryRequestDto, Member member){
        this.title = diaryRequestDto.getTitle();
        this.content = diaryRequestDto.getContent();
        this.member = member;
    }

    public void addImages(List<String> imagesUrls) {
        this.images.addAll(imagesUrls);
    }

    public void update(DiaryRequestDto diaryRequestDto, Member member){
        this.title = diaryRequestDto.getTitle();
        this.content = diaryRequestDto.getContent();
        this.member = member;
    }
}
