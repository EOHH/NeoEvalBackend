package com.neoeval.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import com.neoeval.backend.entity.User;
import com.neoeval.backend.entity.ClassGroup;

@Entity
@Table(name = "practice_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PracticeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private ClassGroup classGroup;
}
