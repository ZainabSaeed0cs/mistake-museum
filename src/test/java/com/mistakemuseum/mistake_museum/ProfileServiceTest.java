package com.mistakemuseum.mistake_museum;

import com.mistakemuseum.mistake_museum.model.Label;
import com.mistakemuseum.mistake_museum.model.Mistake;
import com.mistakemuseum.mistake_museum.model.ProfileResponse;
import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.repository.MistakeRepository;
import com.mistakemuseum.mistake_museum.repository.StudentRepository;
import com.mistakemuseum.mistake_museum.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(ProfileService.class)
class ProfileServiceTest {

    @Autowired StudentRepository students;
    @Autowired MistakeRepository mistakes;
    @Autowired ProfileService profileService;

    private void save(Student s, Label label, String topic) {
        Mistake m = new Mistake();
        m.setStudent(s);
        m.setTopic(topic);
        m.setAiLabel(label);
        mistakes.save(m);
    }

    @Test
    void findsTopPatternAndSentence() {
        Student s = students.save(new Student());
        Label top = Label.values()[0];
        Label other = Label.values()[1];
        save(s, top, "Kinematics");
        save(s, top, "Kinematics");
        save(s, other, "Algebra");

        ProfileResponse p = profileService.buildProfile(s);

        assertEquals(3, p.totalMistakes());
        assertEquals(top.name(), p.topLabel());
        assertEquals("Kinematics", p.topTopic());
        assertTrue(p.sentence().startsWith("67%"));
    }

    @Test
    void emptyProfileHasFriendlyMessage() {
        Student s = students.save(new Student());
        ProfileResponse p = profileService.buildProfile(s);
        assertEquals(0, p.totalMistakes());
        assertTrue(p.sentence().startsWith("No mistakes"));
    }
}