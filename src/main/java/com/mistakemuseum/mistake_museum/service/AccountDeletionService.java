package com.mistakemuseum.mistake_museum.service;

import com.mistakemuseum.mistake_museum.repository.AttemptRepository;
import com.mistakemuseum.mistake_museum.repository.DiagnosisRepository;
import com.mistakemuseum.mistake_museum.repository.MistakeRepository;
import com.mistakemuseum.mistake_museum.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountDeletionService {

    private final DiagnosisRepository diagnosisRepository;
    private final AttemptRepository attemptRepository;
    private final MistakeRepository mistakeRepository;
    private final StudentRepository studentRepository;

    public AccountDeletionService(DiagnosisRepository diagnosisRepository,
                                  AttemptRepository attemptRepository,
                                  MistakeRepository mistakeRepository,
                                  StudentRepository studentRepository) {
        this.diagnosisRepository = diagnosisRepository;
        this.attemptRepository = attemptRepository;
        this.mistakeRepository = mistakeRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public void deleteStudentData(Long studentId) {
        diagnosisRepository.deleteByAttemptStudentId(studentId);
        attemptRepository.deleteByStudentId(studentId);
        mistakeRepository.deleteByStudentId(studentId);
        studentRepository.deleteById(studentId);
    }
}