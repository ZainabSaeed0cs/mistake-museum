package com.mistakemuseum.mistake_museum.controller;

import com.mistakemuseum.mistake_museum.model.Label;
import com.mistakemuseum.mistake_museum.model.Mistake;
import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.repository.MistakeRepository;
import com.mistakemuseum.mistake_museum.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class MistakeController {

    private final StudentService studentService;
    private final MistakeRepository mistakeRepository;

    public MistakeController(StudentService studentService, MistakeRepository mistakeRepository) {
        this.studentService = studentService;
        this.mistakeRepository = mistakeRepository;
    }

    // Returns the current student's mistakes, newest first
    @GetMapping("/api/mistakes")
    public List<Map<String, Object>> getMyMistakes(Authentication authentication,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) {
        Student student = studentService.getCurrentStudent(authentication, request, response);
        List<Mistake> mistakes = mistakeRepository.findByStudentOrderByCreatedAtDesc(student);

        List<Map<String, Object>> result = new ArrayList<>();
        for (Mistake m : mistakes) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", m.getId());
            item.put("subject", m.getSubject());
            item.put("topic", m.getTopic());
            item.put("aiLabel", m.getAiLabel());
            item.put("studentLabel", m.getStudentLabel());
            item.put("effectiveLabel", m.getEffectiveLabel());
            item.put("reason", m.getReason());
            item.put("confidence", m.getConfidence());
            item.put("errorStep", m.getErrorStep());
            item.put("createdAt", m.getCreatedAt());
            result.add(item);
        }
        return result;
    }

    // The student corrects the AI's label. Only their own mistakes can be changed.
    @PostMapping("/api/mistakes/{id}/label")
    public ResponseEntity<?> correctLabel(@PathVariable Long id,
                                          @RequestBody Map<String, String> body,
                                          Authentication authentication,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        Label newLabel;
        try {
            newLabel = Label.valueOf(String.valueOf(body.get("label")).trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Please pick one of the five labels."));
        }

        Student student = studentService.getCurrentStudent(authentication, request, response);
        Optional<Mistake> found = mistakeRepository.findById(id);

        if (found.isEmpty() || !found.get().getStudent().getId().equals(student.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "We could not find that mistake."));
        }

        Mistake mistake = found.get();
        mistake.setStudentLabel(newLabel);
        mistakeRepository.save(mistake);

        return ResponseEntity.ok(Map.of("id", mistake.getId(),
                "effectiveLabel", mistake.getEffectiveLabel().name()));
    }
}