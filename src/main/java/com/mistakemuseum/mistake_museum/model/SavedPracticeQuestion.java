
package com.mistakemuseum.mistake_museum.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "saved_practice_questions")
public class SavedPracticeQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Student student;

    private String subject;

    private String topic;

    private String difficulty;

    @Column(length = 3000)
    private String questionText;

    @Column(length = 3000)
    private String answer;

    @Column(length = 5000)
    private String explanation;

    private Instant createdAt = Instant.now();

    public SavedPracticeQuestion() {
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

