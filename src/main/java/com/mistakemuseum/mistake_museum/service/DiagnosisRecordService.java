
package com.mistakemuseum.mistake_museum.service;

import com.mistakemuseum.mistake_museum.model.Diagnosis;
import com.mistakemuseum.mistake_museum.model.DiagnosisResult;
import com.mistakemuseum.mistake_museum.model.Attempt;
import com.mistakemuseum.mistake_museum.repository.DiagnosisRepository;
import org.springframework.stereotype.Service;

@Service
public class DiagnosisRecordService {

    private final DiagnosisRepository diagnosisRepository;

    public DiagnosisRecordService(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    public Diagnosis saveDiagnosis(Attempt attempt, DiagnosisResult result) {
        Diagnosis diagnosis = new Diagnosis();

        diagnosis.setAttempt(attempt);
        diagnosis.setLabel(result.label());
        diagnosis.setReason(result.reason());
        diagnosis.setTopic(result.topic());
        diagnosis.setConfidence(result.confidence());
        diagnosis.setErrorStep(result.errorStep());

        return diagnosisRepository.save(diagnosis);
    }
}
