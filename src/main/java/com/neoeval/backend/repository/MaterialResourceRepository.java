package com.neoeval.backend.repository;

import com.neoeval.backend.entity.MaterialResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialResourceRepository extends JpaRepository<MaterialResource, Long> {

    // Buscar todos los recursos de una sesión de clase específica
    List<MaterialResource> findByClassSessionId(Long classSessionId);

    // Buscar un recurso por ID y asegurarse de que pertenezca a una sesión
    MaterialResource findByIdAndClassSessionId(Long id, Long classSessionId);
}