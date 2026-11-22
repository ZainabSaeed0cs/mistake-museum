
package com.mistakemuseum.mistake_museum.service;

import com.mistakemuseum.mistake_museum.model.DiagnosisResult;
import com.mistakemuseum.mistake_museum.model.Label;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StubDiagnosisService implements DiagnosisService {

    @Override
    public DiagnosisResult diagnose(
            String subject,
            String topic,
            String question,
            String studentAnswer,
            String correction,
            MultipartFile photo
    ) {
        return new DiagnosisResult(
                Label.MISREAD_QUESTION,
                "You did not convert units first.",
                topic,
                0.8,
                "Step 1"
        );
    }
}

