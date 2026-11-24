package com.mistakemuseum.mistake_museum.service;

import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.repository.StudentRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Finds the current student from the "studentId" cookie, or creates a new one
@Service
public class StudentService {

    private static final String COOKIE_NAME = "studentId";
    private static final int ONE_YEAR_IN_SECONDS = 60 * 60 * 24 * 365;

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student getOrCreateStudent(HttpServletRequest request, HttpServletResponse response) {
        String cookieValue = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    cookieValue = cookie.getValue();
                }
            }
        }

        if (cookieValue != null) {
            Optional<Student> existing = studentRepository.findByAnonymousId(cookieValue);
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        Student student = studentRepository.save(new Student());
        Cookie newCookie = new Cookie(COOKIE_NAME, student.getAnonymousId());
        newCookie.setPath("/");
        newCookie.setHttpOnly(true);
        newCookie.setMaxAge(ONE_YEAR_IN_SECONDS);
        response.addCookie(newCookie);
        return student;
    }
}