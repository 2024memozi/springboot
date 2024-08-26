package com.project.memozi.color.repository;

import com.project.memozi.color.entity.Color;
import com.project.memozi.color.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColorRepository extends JpaRepository<Color,Long> {
    Optional<Color> findByIdAndType(Long id, Type type);
}
