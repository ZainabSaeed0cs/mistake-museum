package com.mistakemuseum.mistake_museum.repository;

import com.mistakemuseum.mistake_museum.model.Mistake;
import com.mistakemuseum.mistake_museum.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MistakeRepository extends JpaRepository<Mistake, Long> {

    // All mistakes of one student, newest first
    List<Mistake> findByStudentOrderByCreatedAtDesc(Student student);
    List<Mistake> findByStudentId(Long studentId);
}