
package com.mistakemuseum.mistake_museum.service;

import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Student registerStudent(String name, String email, String password) {
        if (studentRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("This email is already registered.");
        }

        String hashedPassword = passwordEncoder.encode(password);
        return studentRepository.save(new Student(name, email, hashedPassword));
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
}
