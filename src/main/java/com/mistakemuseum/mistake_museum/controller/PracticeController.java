
package com.mistakemuseum.mistake_museum.controller;

import com.mistakemuseum.mistake_museum.model.Label;
import com.mistakemuseum.mistake_museum.model.PracticeQuestion;
import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.service.PracticeService;
import com.mistakemuseum.mistake_museum.service.ProfileService;
import com.mistakemuseum.mistake_museum.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PracticeController {

    private final StudentService studentService;
    private final ProfileService profileService;
    private final PracticeService practiceService;

    public PracticeController(
            StudentService studentService,
            ProfileService profileService,
            PracticeService practiceService) {
        this.studentService = studentService;
        this.profileService = profileService;
        this.practiceService = practiceService;
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
}
