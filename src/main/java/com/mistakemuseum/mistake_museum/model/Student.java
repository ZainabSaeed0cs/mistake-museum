<<<<<<< HEAD
package com.mistakemuseum.mistake_museum.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

// One row per anonymous student. No names or contact details are stored.
@Entity
=======

package com.mistakemuseum.mistake_museum.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
>>>>>>> origin/main
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

<<<<<<< HEAD
    // Random id we also keep in the browser cookie "studentId"
    @Column(nullable = false, unique = true)
    private String anonymousId = UUID.randomUUID().toString();

    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public String getAnonymousId() { return anonymousId; }
    public Instant getCreatedAt() { return createdAt; }
}
=======
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Student() {
    }

    public Student(String name, String email, String passwordHash) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
>>>>>>> origin/main
