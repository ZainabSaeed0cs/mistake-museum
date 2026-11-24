package com.mistakemuseum.mistake_museum.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

// One row per anonymous student. No names or contact details are stored.
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Random id we also keep in the browser cookie "studentId"
    @Column(nullable = false, unique = true)
    private String anonymousId = UUID.randomUUID().toString();

    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public String getAnonymousId() { return anonymousId; }
    public Instant getCreatedAt() { return createdAt; }
}