
package com.mistakemuseum.mistake_museum.service;

import com.mistakemuseum.mistake_museum.model.DiagnosisResult;
import org.springframework.web.multipart.MultipartFile;

public interface DiagnosisService {

    DiagnosisResult diagnose(
            String subject,
            String topic,
            String question,
            String studentAnswer,
            String correction,
            MultipartFile photo
    );
}
