package com.project.memozi.kakao.entity;

import com.project.memozi.category.entity.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String kakaoId;

    private String nickname;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Category> categories;

}
