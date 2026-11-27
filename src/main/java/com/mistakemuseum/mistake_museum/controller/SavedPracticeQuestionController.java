
package com.mistakemuseum.mistake_museum.controller;

import com.mistakemuseum.mistake_museum.model.SavedPracticeQuestion;
import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.repository.SavedPracticeQuestionRepository;
import com.mistakemuseum.mistake_museum.service.StudentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class SavedPracticeQuestionController {

    private final StudentService studentService;
    private final SavedPracticeQuestionRepository savedRepository;

    public SavedPracticeQuestionController(
            StudentService studentService,
            SavedPracticeQuestionRepository savedRepository) {
        this.studentService = studentService;
        this.savedRepository = savedRepository;
    }

    @PostMapping("/api/practice/saved")
    public SaveResponse saveQuestion(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody SaveRequest body) {

        Student student = studentService.getCurrentStudent(
                authentication, request, response);

        if (body.questionText() == null || body.questionText().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Question text is required");
        }

        var existing = savedRepository
                .findByStudentAndQuestionTextIgnoreCase(
                        student, body.questionText().trim());

        if (existing.isPresent()) {
            return new SaveResponse(
                    existing.get().getId(), true, "Already saved");
        }

        SavedPracticeQuestion saved = new SavedPracticeQuestion();
        saved.setStudent(student);
        saved.setSubject(body.subject());
        saved.setTopic(body.topic());
        saved.setDifficulty(body.difficulty());
        saved.setQuestionText(body.questionText().trim());
        saved.setAnswer(body.answer());
        saved.setExplanation(body.explanation());

        SavedPracticeQuestion result = savedRepository.save(saved);

        return new SaveResponse(result.getId(), true, "Question saved");
    }

    @DeleteMapping("/api/practice/saved/{id}")
    public SaveResponse unsaveQuestion(
            @PathVariable Long id,
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response) {

        Student student = studentService.getCurrentStudent(
                authentication, request, response);

        SavedPracticeQuestion saved = savedRepository
                .findByIdAndStudent(id, student)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Saved question not found"));

        savedRepository.delete(saved);

        return new SaveResponse(id, false, "Question removed from saved questions");
    }

    public record SaveRequest(
            String subject,
            String topic,
            String difficulty,
            String questionText,
            String answer,
            String explanation
    ) {
    }

    public record SaveResponse(
            Long id,
            boolean saved,
            String message
    ) {
    }
}
