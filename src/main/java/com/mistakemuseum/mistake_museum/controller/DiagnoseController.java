package com.mistakemuseum.mistake_museum.controller;

import com.mistakemuseum.mistake_museum.model.DiagnosisResult;
import com.mistakemuseum.mistake_museum.service.DiagnosisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DiagnoseController {

    private final DiagnosisService diagnosisService;

    public DiagnoseController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @PostMapping(value = "/diagnose", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> diagnose(
            @RequestParam String subject,
            @RequestParam String topic,
            @RequestParam(required = false) String question,
            @RequestParam(required = false) String studentAnswer,
            @RequestParam(required = false) String correction,
            @RequestParam(required = false) MultipartFile photo
    ) {

        if (subject.isBlank() || topic.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Please enter your subject and topic."));
        }

        boolean hasPhoto = photo != null && !photo.isEmpty();
        boolean hasTypedAnswer = question != null && !question.isBlank()
                && studentAnswer != null && !studentAnswer.isBlank()
                && correction != null && !correction.isBlank();

        if (!hasPhoto && !hasTypedAnswer) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Please upload a photo or fill in all three answer fields."));
        }

        if (hasPhoto) {
            if (photo.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Your image must be smaller than 5 MB."));
            }

            String contentType = photo.getContentType();

            if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Please upload a JPG or PNG image."));
            }
        }

        try {
            DiagnosisResult result = diagnosisService.diagnose(
                    subject, topic, question, studentAnswer, correction, photo
            );
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong. Please try again."));
        }
    }
}