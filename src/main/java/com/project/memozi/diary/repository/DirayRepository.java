package com.project.memozi.diary.repository;

import com.project.memozi.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirayRepository extends JpaRepository<Diary,Long> {

}
