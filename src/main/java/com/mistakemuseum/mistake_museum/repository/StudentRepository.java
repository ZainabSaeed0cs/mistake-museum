package com.mistakemuseum.mistake_museum.repository;

import com.mistakemuseum.mistake_museum.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Spring creates the database code for this automatically
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Finds a student by the id stored in the browser cookie
    Optional<Student> findByAnonymousId(String anonymousId);
}