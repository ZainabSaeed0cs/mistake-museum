package com.mistakemuseum.mistake_museum.controller;

import com.mistakemuseum.mistake_museum.model.Mistake;
import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.repository.MistakeRepository;
import com.mistakemuseum.mistake_museum.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public List<Map<String, Object>> getMyMistakes(HttpServletRequest request, HttpServletResponse response) {
        Student student = studentService.getOrCreateStudent(request, response);
        List<Mistake> mistakes = mistakeRepository.findByStudentOrderByCreatedAtDesc(student);

        // We build simple maps so the JSON stays small and has no student details
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
}