
package com.mistakemuseum.mistake_museum.controller;

import com.mistakemuseum.mistake_museum.model.SavedPracticeQuestion;
import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.repository.SavedPracticeQuestionRepository;
import com.mistakemuseum.mistake_museum.service.StudentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SavedPracticeQuestionPageController {

    private final StudentService studentService;
    private final SavedPracticeQuestionRepository savedRepository;

    public SavedPracticeQuestionPageController(
            StudentService studentService,
            SavedPracticeQuestionRepository savedRepository) {
        this.studentService = studentService;
        this.savedRepository = savedRepository;
    }

    @PostMapping("/history/saved/{id}/delete")
    public String unsaveQuestion(
            @PathVariable Long id,
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response) {

        Student student = studentService.getCurrentStudent(
                authentication, request, response);

        SavedPracticeQuestion saved = savedRepository
                .findByIdAndStudent(id, student)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Saved question not found"));

        String subject = saved.getSubject();

        savedRepository.delete(saved);

        return "redirect:/history/subject/" +
                org.springframework.web.util.UriUtils.encodePathSegment(
                        subject, java.nio.charset.StandardCharsets.UTF_8);
    }
}
