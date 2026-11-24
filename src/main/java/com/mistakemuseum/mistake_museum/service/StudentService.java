
package com.mistakemuseum.mistake_museum.service;

import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.repository.StudentRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private static final String COOKIE_NAME = "studentId";
    private static final int ONE_YEAR_IN_SECONDS = 60 * 60 * 24 * 365;

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Student getOrCreateStudent(
            HttpServletRequest request,
            HttpServletResponse response) {

        String cookieValue = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }

        if (cookieValue != null) {
            Optional<Student> existing =
                    studentRepository.findByAnonymousId(cookieValue);

            if (existing.isPresent()) {
                return existing.get();
            }
        }

        Student student = studentRepository.save(new Student());

        Cookie newCookie =
                new Cookie(COOKIE_NAME, student.getAnonymousId());

        newCookie.setPath("/");
        newCookie.setHttpOnly(true);
        newCookie.setMaxAge(ONE_YEAR_IN_SECONDS);

        response.addCookie(newCookie);

        return student;
    }

    public Student registerStudent(String name, String email, String password) {

        if (studentRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException(
                    "This email is already registered.");
        }

        String hashedPassword = passwordEncoder.encode(password);

        return studentRepository.save(
                new Student(name, email, hashedPassword));
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
