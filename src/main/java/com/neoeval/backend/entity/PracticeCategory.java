package com.neoeval.backend.entity;

import jakarta.persistence.*;
import lombok.*;

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
}
