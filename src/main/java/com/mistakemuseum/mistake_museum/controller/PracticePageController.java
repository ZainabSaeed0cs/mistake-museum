package com.mistakemuseum.mistake_museum.controller;

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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class PracticePageController {

    private final StudentService studentService;
    private final MistakeRepository mistakeRepository;

    public PracticePageController(StudentService studentService,
                                  MistakeRepository mistakeRepository) {
        this.studentService = studentService;
        this.mistakeRepository = mistakeRepository;
    }

    @GetMapping("/practice")
    public String practiceRoom(Authentication authentication,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               Model model) {
        Student student = studentService.getCurrentStudent(authentication, request, response);
        List<Mistake> mistakes = mistakeRepository.findByStudentOrderByCreatedAtDesc(student);

        Map<String, Integer> topicCounts = new LinkedHashMap<>();
        Map<String, Integer> subjectCounts = new LinkedHashMap<>();
        Map<String, Integer> mistakeTypeCounts = new LinkedHashMap<>();
        Set<String> subjects = new LinkedHashSet<>();
        Set<String> topics = new LinkedHashSet<>();

        for (Mistake mistake : mistakes) {
            if (mistake == null) {
                continue;
            }

            String subject = mistake.getSubject() != null ? mistake.getSubject().trim() : "";
            String topic = mistake.getTopic() != null ? mistake.getTopic().trim() : "";

            if (!subject.isEmpty()) {
                subjectCounts.put(subject, subjectCounts.getOrDefault(subject, 0) + 1);
                subjects.add(subject);
            }

            if (!topic.isEmpty()) {
                topicCounts.put(topic, topicCounts.getOrDefault(topic, 0) + 1);
                topics.add(topic);
            }

            if (mistake.getEffectiveLabel() != null) {
                String labelName = formatLabel(mistake.getEffectiveLabel().name());
                mistakeTypeCounts.put(labelName, mistakeTypeCounts.getOrDefault(labelName, 0) + 1);
            }
        }

        List<CountItem> sortedTopics = toSortedList(topicCounts);
        List<CountItem> sortedSubjects = toSortedList(subjectCounts);
        List<CountItem> sortedMistakeTypes = toSortedList(mistakeTypeCounts);

        String topMistakeType = sortedMistakeTypes.isEmpty()
                ? "Repeated practice needed"
                : sortedMistakeTypes.get(0).getLabel();

        List<WeakAreaItem> weakAreas = new ArrayList<>();
        for (CountItem item : sortedTopics) {
            weakAreas.add(new WeakAreaItem(item.getLabel(), item.getCount(), topMistakeType));
        }

        model.addAttribute("hasWeakAreas", !weakAreas.isEmpty());
        model.addAttribute("weakAreas", weakAreas);
        model.addAttribute("subjectCounts", sortedSubjects);
        model.addAttribute("mistakeTypeCounts", sortedMistakeTypes);
        model.addAttribute("subjects", new ArrayList<>(subjects));
        model.addAttribute("topics", new ArrayList<>(topics));
        model.addAttribute("questions", List.of());

        return "practice";
    }

    private List<CountItem> toSortedList(Map<String, Integer> counts) {
        List<CountItem> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            list.add(new CountItem(entry.getKey(), entry.getValue()));
        }
        list.sort(Comparator.comparingInt(CountItem::getCount).reversed()
                .thenComparing(CountItem::getLabel));
        return list;
    }

    private String formatLabel(String rawName) {
        if (rawName == null || rawName.isBlank()) {
            return "Other";
        }
        String[] parts = rawName.toLowerCase().split("_");
        StringBuilder formatted = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                if (formatted.length() > 0) {
                    formatted.append(" ");
                }
                formatted.append(Character.toUpperCase(part.charAt(0)))
                         .append(part.substring(1));
            }
        }
        return formatted.toString();
    }

    public static class CountItem {
        private final String label;
        private final int count;

        public CountItem(String label, int count) {
            this.label = label;
            this.count = count;
        }

        public String getLabel() { return label; }
        public int getCount() { return count; }
    }

    public static class WeakAreaItem {
        private final String topic;
        private final int count;
        private final String highlight;

        public WeakAreaItem(String topic, int count, String highlight) {
            this.topic = topic;
            this.count = count;
            this.highlight = highlight;
        }

        public String getTopic() { return topic; }
        public int getCount() { return count; }
        public String getHighlight() { return highlight; }
    }

    public static class PracticeQuestionCard {
        private final int number;
        private final String subjectAndTopic;
        private final String questionText;
        private final String answer;
        private final String explanation;

        public PracticeQuestionCard(int number, String subjectAndTopic, String questionText, String answer, String explanation) {
            this.number = number;
            this.subjectAndTopic = subjectAndTopic;
            this.questionText = questionText;
            this.answer = answer;
            this.explanation = explanation;
        }

        public int getNumber() { return number; }
        public String getSubjectAndTopic() { return subjectAndTopic; }
        public String getQuestionText() { return questionText; }
        public String getAnswer() { return answer; }
        public String getExplanation() { return explanation; }
    }
}