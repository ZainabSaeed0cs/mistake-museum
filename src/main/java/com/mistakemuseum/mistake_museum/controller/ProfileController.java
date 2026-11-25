package com.mistakemuseum.mistake_museum.controller;

import com.mistakemuseum.mistake_museum.model.ProfileResponse;
import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.service.ProfileService;
import com.mistakemuseum.mistake_museum.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    private final StudentService studentService;
    private final ProfileService profileService;

    public ProfileController(StudentService studentService, ProfileService profileService) {
        this.studentService = studentService;
        this.profileService = profileService;
    }

    @GetMapping("/api/profile")
    public ProfileResponse profile(HttpServletRequest request, HttpServletResponse response) {
        Student student = studentService.getOrCreateStudent(request, response);
        return profileService.buildProfile(student);
    }
}