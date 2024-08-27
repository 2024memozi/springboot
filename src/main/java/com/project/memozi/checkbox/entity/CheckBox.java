package com.project.memozi.checkbox.entity;

import com.project.memozi.memo.entity.Memo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkbox_id")
    Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id")
    private Memo memo;

    public CheckBox(String content, Memo memo) {
        this.content = content;
        this.memo =memo;
        this.isChecked = false;
    }

    public void doCheck() {
        this.isChecked = !this.isChecked;
    }

    public void setMemo(Memo memo) {
        this.memo = memo;
    }
}
