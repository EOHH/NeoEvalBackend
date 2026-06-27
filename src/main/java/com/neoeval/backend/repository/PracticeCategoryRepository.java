package com.neoeval.backend.repository;

import com.neoeval.backend.entity.PracticeCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeCategoryRepository extends JpaRepository<PracticeCategory, Long> {
    Page<PracticeCategory> findByActiveTrue(Pageable pageable);
    Page<PracticeCategory> findByTeacher_Id(Long teacherId, Pageable pageable);
    Page<PracticeCategory> findByClassGroup_IdAndActiveTrue(Long groupId, Pageable pageable);
}
