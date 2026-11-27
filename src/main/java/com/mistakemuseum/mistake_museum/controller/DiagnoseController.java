package com.mistakemuseum.mistake_museum.controller;

import com.mistakemuseum.mistake_museum.model.DiagnosisResult;
import com.mistakemuseum.mistake_museum.model.Mistake;
import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.repository.MistakeRepository;
import com.mistakemuseum.mistake_museum.service.DiagnosisService;
import com.mistakemuseum.mistake_museum.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DiagnoseController {

    private static final Logger log = LoggerFactory.getLogger(DiagnoseController.class);

    private final DiagnosisService diagnosisService;
    private final StudentService studentService;
    private final MistakeRepository mistakeRepository;

    public DiagnoseController(DiagnosisService diagnosisService,
                              StudentService studentService,
                              MistakeRepository mistakeRepository) {
        this.diagnosisService = diagnosisService;
        this.studentService = studentService;
        this.mistakeRepository = mistakeRepository;
    }

    @PostMapping(value = "/diagnose", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> diagnose(
            @RequestParam String subject,
            @RequestParam String topic,
            @RequestParam(required = false) String question,
            @RequestParam(required = false) String studentAnswer,
            @RequestParam(required = false) String correction,
            @RequestParam(required = false) MultipartFile photo,
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (subject.isBlank() || topic.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Please enter your subject and topic."));
        }

        boolean hasPhoto = photo != null && !photo.isEmpty();
        boolean hasTypedAnswer = question != null && !question.isBlank()
        && studentAnswer != null && !studentAnswer.isBlank();

        if (!hasPhoto && !hasTypedAnswer) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Please upload a photo or fill in the question and your answer."));
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

            Long mistakeId = saveMistake(authentication, request, response, subject, topic, result);

            java.util.Map<String, Object> out = new java.util.LinkedHashMap<>();
            out.put("label", result.label() == null ? null : result.label().name());
            out.put("reason", result.reason());
            out.put("topic", result.topic());
            out.put("confidence", result.confidence());
            out.put("errorStep", result.errorStep());
            out.put("mistakeId", mistakeId);
            return ResponseEntity.ok(out);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            log.error("Diagnose request failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong. Please try again."));
        }
    }

    // A save problem must not hide a good diagnosis from the student
    private Long saveMistake(Authentication authentication,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             String subject,
                             String topic,
                             DiagnosisResult result) {
        try {
            Student student = studentService.getCurrentStudent(authentication, request, response);

            Mistake mistake = new Mistake();
            mistake.setStudent(student);
            mistake.setSubject(cut(subject, 255));
            mistake.setTopic(cut(topic, 255));
            mistake.setAiLabel(result.label());
            mistake.setReason(cut(result.reason(), 1000));
            mistake.setConfidence(result.confidence());
            mistake.setErrorStep(cut(result.errorStep(), 255));

            return mistakeRepository.save(mistake).getId();
        } catch (RuntimeException e) {
            log.error("Could not save the mistake", e);
            return null;
        }
    }

    private static String cut(String s, int max) {
        if (s == null) {
            return null;
        }
        return s.length() <= max ? s : s.substring(0, max);
    }
}