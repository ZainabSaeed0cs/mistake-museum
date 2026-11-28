
package com.mistakemuseum.mistake_museum.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "practice_results")
public class PracticeResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Student student;

    private String subject;

    private String topic;

    @Column(length = 3000)
    private String questionText;

    private boolean correct;

    private Instant createdAt = Instant.now();

    public PracticeResult() {
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

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
