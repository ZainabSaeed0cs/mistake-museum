
package com.mistakemuseum.mistake_museum.repository;

import com.mistakemuseum.mistake_museum.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

}
