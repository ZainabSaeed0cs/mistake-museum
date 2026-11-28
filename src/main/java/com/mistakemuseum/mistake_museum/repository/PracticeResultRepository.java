
package com.mistakemuseum.mistake_museum.repository;

import com.mistakemuseum.mistake_museum.model.PracticeResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PracticeResultRepository
        extends JpaRepository<PracticeResult, Long> {

    long countByStudentId(Long studentId);

    long countByStudentIdAndCorrectTrue(Long studentId);

    void deleteByStudentId(Long studentId);
}
