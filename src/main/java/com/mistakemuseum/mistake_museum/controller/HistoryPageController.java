
package com.mistakemuseum.mistake_museum.controller;

import org.springframework.web.bind.annotation.PathVariable;
import com.mistakemuseum.mistake_museum.model.Mistake;
import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.repository.MistakeRepository;
import com.mistakemuseum.mistake_museum.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class HistoryPageController {

    private final StudentService studentService;
    private final MistakeRepository mistakeRepository;

    public HistoryPageController(
            StudentService studentService,
            MistakeRepository mistakeRepository) {
        this.studentService = studentService;
        this.mistakeRepository = mistakeRepository;
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

        Map<String, Long> subjectCounts = new TreeMap<>();

        for (Mistake mistake : mistakes) {
            String subject = mistake.getSubject();

            if (subject != null && !subject.isBlank()) {
                subjectCounts.merge(subject.trim(), 1L, Long::sum);
            }
        }

        model.addAttribute("subjectCounts", subjectCounts);
        model.addAttribute("totalMistakes", mistakes.size());

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

    model.addAttribute("subject", subject);
    model.addAttribute("mistakes", subjectMistakes);

    return "history-subject";
}
}
