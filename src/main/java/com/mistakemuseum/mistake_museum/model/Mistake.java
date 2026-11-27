package com.mistakemuseum.mistake_museum.model;

import jakarta.persistence.*;
import java.time.Instant;

// One row per diagnosed mistake. It belongs to one anonymous Student.
@Entity
public class Mistake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many mistakes can belong to one student
    @ManyToOne(optional = false)
    private Student student;

    private String subject;
    private String topic;
    @Column(length = 2000)
private String question;

@Column(length = 5000)
private String studentAnswer;

@Column(length = 5000)
private String correction;
    // Label chosen by the AI
    @Enumerated(EnumType.STRING)
    private Label aiLabel;

    // Label chosen by the student if they correct the AI (can be null)
    @Enumerated(EnumType.STRING)
    private Label studentLabel;

    @Column(length = 1000)
    private String reason;

    private double confidence;
    private String errorStep;

    // Path of the uploaded image (can be null)
    private String imagePath;

    private Instant createdAt = Instant.now();

    // The label we show everywhere: the student's correction if present, otherwise the AI's
    public Label getEffectiveLabel() {
        return studentLabel != null ? studentLabel : aiLabel;
    }

    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public Label getAiLabel() { return aiLabel; }
    public void setAiLabel(Label aiLabel) { this.aiLabel = aiLabel; }
    public Label getStudentLabel() { return studentLabel; }
    public void setStudentLabel(Label studentLabel) { this.studentLabel = studentLabel; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public String getErrorStep() { return errorStep; }
    public void setErrorStep(String errorStep) { this.errorStep = errorStep; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public Instant getCreatedAt() { return createdAt; }
    public String getQuestion() { return question; }
public void setQuestion(String question) { this.question = question; }

public String getStudentAnswer() { return studentAnswer; }
public void setStudentAnswer(String studentAnswer) { this.studentAnswer = studentAnswer; }

public String getCorrection() { return correction; }
public void setCorrection(String correction) { this.correction = correction; }
}