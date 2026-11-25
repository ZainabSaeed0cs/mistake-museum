package com.mistakemuseum.mistake_museum;

import com.mistakemuseum.mistake_museum.model.Label;
import com.mistakemuseum.mistake_museum.service.PracticeService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PracticeServiceTest {

    @Test
    void returnsFiveQuestionsForEveryLabel() {
        PracticeService service = new PracticeService();
        for (Label label : Label.values()) {
            assertEquals(5, service.generate(label).size());
        }
    }
}