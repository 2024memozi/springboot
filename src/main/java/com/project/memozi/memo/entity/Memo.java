package com.project.memozi.memo.entity;

import com.project.memozi.util.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
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

    @PrePersist
    @PreUpdate
    private void setDay(){
        LocalDateTime dateTime = getUpdatedAt() != null ? getUpdatedAt() : getCreatedAt();
        this.dayOfWeek = dateTime.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.KOREAN);
    }

}
