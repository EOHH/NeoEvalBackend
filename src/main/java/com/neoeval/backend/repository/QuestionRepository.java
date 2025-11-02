// src/main/java/com/neoeval/backend/repository/QuestionRepository.java
package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByExamId(Long examId); // Añade este método
}