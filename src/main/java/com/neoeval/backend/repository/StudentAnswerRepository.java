package com.neoeval.backend.repository;

import com.neoeval.backend.entity.StudentAnswer;
import com.neoeval.backend.entity.StudentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {

    /**
     * Obtiene todas las respuestas enviadas para un resultado de quiz específico.
     * JOIN FETCH es usado para cargar la Question asociada de una sola vez.
     */
    @Query("SELECT sa FROM StudentAnswer sa JOIN FETCH sa.question q LEFT JOIN FETCH q.answers a WHERE sa.studentResult.id = :resultId")
    List<StudentAnswer> findByStudentResultId(@Param("resultId") Long resultId);

    @Query("SELECT sa FROM StudentAnswer sa WHERE sa.studentResult = :result AND sa.question.id = :questionId")
    Optional<StudentAnswer> findByStudentResultAndQuestionId(
            @Param("result") StudentResult result,
            @Param("questionId") Long questionId
    );
}