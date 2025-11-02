package com.neoeval.backend.repository;

import com.neoeval.backend.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Asegúrate de que está anotado con @Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    // save ya está provisto por JpaRepository.
}