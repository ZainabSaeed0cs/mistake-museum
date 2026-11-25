package com.mistakemuseum.mistake_museum.service;

import com.mistakemuseum.mistake_museum.model.Label;
import com.mistakemuseum.mistake_museum.model.Mistake;
import com.mistakemuseum.mistake_museum.model.ProfileResponse;
import com.mistakemuseum.mistake_museum.model.Student;
import com.mistakemuseum.mistake_museum.repository.MistakeRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class ProfileService {

    private final MistakeRepository mistakes;

    public ProfileService(MistakeRepository mistakes) {
        this.mistakes = mistakes;
    }

    public ProfileResponse buildProfile(Student student) {
        List<Mistake> list = mistakes.findByStudentId(student.getId());
        int total = list.size();

        // 1. Count mistakes per label (the student's correction counts if it exists)
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (Label l : Label.values()) {
            counts.put(l.name(), 0);
        }
        for (Mistake m : list) {
            Label label = m.getEffectiveLabel();
            if (label != null) {
                counts.merge(label.name(), 1, Integer::sum);
            }
        }

        // 2. Find the most common label
        String topLabel = null;
        int topCount = 0;
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            if (e.getValue() > topCount) {
                topLabel = e.getKey();
                topCount = e.getValue();
            }
        }

        // 3. Find the most common topic inside that label
        String topTopic = null;
        if (topLabel != null) {
            Map<String, Integer> topics = new HashMap<>();
            for (Mistake m : list) {
                Label l = m.getEffectiveLabel();
                if (l != null && l.name().equals(topLabel)
                        && m.getTopic() != null && !m.getTopic().isBlank()) {
                    topics.merge(m.getTopic().trim(), 1, Integer::sum);
                }
            }
            int best = 0;
            for (Map.Entry<String, Integer> e : topics.entrySet()) {
                if (e.getValue() > best) {
                    topTopic = e.getKey();
                    best = e.getValue();
                }
            }
        }

        // 4. Count mistakes per week (keyed by the Monday of that week)
        Map<String, Integer> weekly = new TreeMap<>();
        for (Mistake m : list) {
            LocalDate day = m.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate();
            weekly.merge(day.with(DayOfWeek.MONDAY).toString(), 1, Integer::sum);
        }

        // 5. Write the sentence
        String sentence;
        if (total == 0 || topLabel == null) {
            sentence = "No mistakes saved yet. Upload one to start your profile.";
        } else {
            long percent = Math.round(100.0 * topCount / total);
            sentence = percent + "% of your mistakes are "
                    + topLabel.toLowerCase().replace('_', ' ')
                    + (topTopic != null ? ", mostly in " + topTopic : "") + ".";
        }

        return new ProfileResponse(total, counts, topLabel, topTopic, sentence, weekly);
    }
}