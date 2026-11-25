package com.mistakemuseum.mistake_museum;

import com.mistakemuseum.mistake_museum.model.Label;
import com.mistakemuseum.mistake_museum.model.Mistake;
import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.repository.MistakeRepository;
import com.mistakemuseum.mistake_museum.repository.StudentRepository;
import com.mistakemuseum.mistake_museum.service.AccountDeletionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(AccountDeletionService.class)
class AccountDeletionServiceTest {

    @Autowired StudentRepository students;
    @Autowired MistakeRepository mistakes;
    @Autowired AccountDeletionService deletionService;

    private void save(Student s, String topic) {
        Mistake m = new Mistake();
        m.setStudent(s);
        m.setTopic(topic);
        m.setAiLabel(Label.values()[0]);
        mistakes.save(m);
    }

    @Test
    void deletesOnlyTheChosenStudentsData() {
        Student first = students.save(new Student());
        Student second = students.save(new Student());
        save(first, "Kinematics");
        save(second, "Algebra");

        deletionService.deleteStudentData(first.getId());

        // First student and their mistakes are gone
        assertFalse(students.findById(first.getId()).isPresent());
        assertEquals(0, mistakes.findByStudentId(first.getId()).size());

        // Second student's data is still there
        assertTrue(students.findById(second.getId()).isPresent());
        assertEquals(1, mistakes.findByStudentId(second.getId()).size());
    }
}