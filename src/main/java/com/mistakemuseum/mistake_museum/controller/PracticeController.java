
package com.mistakemuseum.mistake_museum.controller;

import com.mistakemuseum.mistake_museum.model.Label;
import com.mistakemuseum.mistake_museum.model.Mistake;
import com.mistakemuseum.mistake_museum.model.PracticeQuestion;
import com.mistakemuseum.mistake_museum.model.GeneratedQuestion;
import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.repository.MistakeRepository;
import com.mistakemuseum.mistake_museum.service.PracticeService;
import com.mistakemuseum.mistake_museum.service.ProfileService;
import com.mistakemuseum.mistake_museum.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@RestController
public class PracticeController {

    private final StudentService studentService;
    private final ProfileService profileService;
    private final PracticeService practiceService;
    private final MistakeRepository mistakeRepository;

    public PracticeController(
            StudentService studentService,
            ProfileService profileService,
            PracticeService practiceService,
            MistakeRepository mistakeRepository) {
        this.studentService = studentService;
        this.profileService = profileService;
        this.practiceService = practiceService;
        this.mistakeRepository = mistakeRepository;
    }

    @GetMapping("/api/practice")
    public List<PracticeQuestion> practice(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response) {

        Student student = studentService.getCurrentStudent(
                authentication, request, response);

        String topLabel = profileService.buildProfile(student).topLabel();

        if (topLabel == null) {
            return List.of();
        }

        return practiceService.generate(Label.valueOf(topLabel));
    }

    @PostMapping("/api/practice/generate")
    public List<GeneratedQuestion> generatePractice(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody PracticeRequest body) {

        Student student = studentService.getCurrentStudent(
                authentication, request, response);

        List<Mistake> mistakes =
                mistakeRepository.findByStudentOrderByCreatedAtDesc(student);

        Set<String> weakTopics = new LinkedHashSet<>();

        for (Mistake mistake : mistakes) {
            if (mistake != null
                    && mistake.getTopic() != null
                    && !mistake.getTopic().isBlank()) {
                weakTopics.add(mistake.getTopic().trim());
            }
        }

        List<String> previousQuestions =
                body.previousQuestions() == null
                        ? new ArrayList<>()
                        : body.previousQuestions();

        return practiceService.generateQuestions(
                body.subject(),
                body.topic(),
                body.difficulty(),
                body.count(),
                new ArrayList<>(weakTopics),
                previousQuestions
        );
    }

    public record PracticeRequest(
            String subject,
            String topic,
            String difficulty,
            int count,
            List<String> previousQuestions
    ) {
    }
}
