package com.project.memozi.diary.repository;

import com.project.memozi.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary,Long> {
    List<Diary> findAllByOrderByCreatedAtDesc();
}
