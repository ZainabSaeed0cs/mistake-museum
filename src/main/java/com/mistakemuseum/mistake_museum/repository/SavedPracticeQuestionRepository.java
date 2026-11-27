
package com.mistakemuseum.mistake_museum.repository;

import com.mistakemuseum.mistake_museum.model.SavedPracticeQuestion;
import com.mistakemuseum.mistake_museum.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedPracticeQuestionRepository
        extends JpaRepository<SavedPracticeQuestion, Long> {

    List<SavedPracticeQuestion> findByStudentOrderByCreatedAtDesc(Student student);

    List<SavedPracticeQuestion> findByStudentAndSubjectIgnoreCaseOrderByCreatedAtDesc(
            Student student, String subject);

    Optional<SavedPracticeQuestion> findByIdAndStudent(Long id, Student student);

    Optional<SavedPracticeQuestion> findByStudentAndQuestionTextIgnoreCase(
            Student student, String questionText);
}
