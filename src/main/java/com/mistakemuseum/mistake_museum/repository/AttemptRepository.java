
package com.mistakemuseum.mistake_museum.repository;

import com.mistakemuseum.mistake_museum.model.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    List<Attempt> findBySubject(String subject);

    List<Attempt> findByTopic(String topic);

}
