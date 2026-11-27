
package com.mistakemuseum.mistake_museum.controller;

import com.mistakemuseum.mistake_museum.model.Mistake;
import com.mistakemuseum.mistake_museum.model.SavedPracticeQuestion;
import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.repository.MistakeRepository;
import com.mistakemuseum.mistake_museum.repository.SavedPracticeQuestionRepository;
import com.mistakemuseum.mistake_museum.service.StudentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class HistoryPageController {

    private final StudentService studentService;
    private final MistakeRepository mistakeRepository;
    private final SavedPracticeQuestionRepository savedRepository;

    public HistoryPageController(
            StudentService studentService,
            MistakeRepository mistakeRepository,
            SavedPracticeQuestionRepository savedRepository) {
        this.studentService = studentService;
        this.mistakeRepository = mistakeRepository;
        this.savedRepository = savedRepository;
    }

    @GetMapping("/history")
    public String history(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {

        Student student = studentService.getCurrentStudent(
                authentication, request, response);

        List<Mistake> mistakes =
                mistakeRepository.findByStudentOrderByCreatedAtDesc(student);

        List<SavedPracticeQuestion> savedQuestions =
                savedRepository.findByStudentOrderByCreatedAtDesc(student);

        Map<String, Long> subjectCounts = new TreeMap<>();

        for (Mistake mistake : mistakes) {
            String subject = mistake.getSubject();
            if (subject != null && !subject.isBlank()) {
                subjectCounts.merge(subject.trim(), 1L, Long::sum);
            }
        }

        for (SavedPracticeQuestion saved : savedQuestions) {
            String subject = saved.getSubject();
            if (subject != null && !subject.isBlank()) {
                subjectCounts.merge(subject.trim(), 1L, Long::sum);
            }
        }

        model.addAttribute("subjectCounts", subjectCounts);
        model.addAttribute("totalMistakes", mistakes.size());
        model.addAttribute("totalLibraryItems",
                mistakes.size() + savedQuestions.size());

        return "history";
    }

    @GetMapping("/history/subject/{subject}")
    public String subjectHistory(
            @PathVariable String subject,
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {

        Student student = studentService.getCurrentStudent(
                authentication, request, response);

        List<Mistake> mistakes =
                mistakeRepository.findByStudentOrderByCreatedAtDesc(student);

        List<Mistake> subjectMistakes = mistakes.stream()
                .filter(m -> m.getSubject() != null
                        && m.getSubject().trim().equalsIgnoreCase(subject.trim()))
                .toList();

        List<SavedPracticeQuestion> savedQuestions =
                savedRepository.findByStudentAndSubjectIgnoreCaseOrderByCreatedAtDesc(
                        student, subject.trim());

        model.addAttribute("subject", subject);
        model.addAttribute("mistakes", subjectMistakes);
        model.addAttribute("savedQuestions", savedQuestions);

        return "history-subject";
    }
}
