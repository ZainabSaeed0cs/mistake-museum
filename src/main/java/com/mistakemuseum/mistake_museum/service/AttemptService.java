
package com.mistakemuseum.mistake_museum.service;

import com.mistakemuseum.mistake_museum.model.Attempt;
import com.mistakemuseum.mistake_museum.repository.AttemptRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttemptService {

    private final AttemptRepository attemptRepository;

    public AttemptService(AttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    public Attempt saveAttempt(Attempt attempt) {
        return attemptRepository.save(attempt);
    }

    public List<Attempt> getAllAttempts() {
        return attemptRepository.findAll();
    }

    public List<Attempt> getAttemptsBySubject(String subject) {
        return attemptRepository.findBySubject(subject);
    }

    public List<Attempt> getAttemptsByTopic(String topic) {
        return attemptRepository.findByTopic(topic);
    }
}
