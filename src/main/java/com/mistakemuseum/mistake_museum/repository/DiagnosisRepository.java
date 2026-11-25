
package com.mistakemuseum.mistake_museum.repository;

import com.mistakemuseum.mistake_museum.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    List<Diagnosis> findByLabel(com.mistakemuseum.mistake_museum.model.Label label);
    void deleteByAttemptStudentId(Long studentId);
}
