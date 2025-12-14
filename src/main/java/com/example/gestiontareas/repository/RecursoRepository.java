package com.example.gestiontareas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.gestiontareas.model.Recurso;

@Repository
public interface RecursoRepository extends JpaRepository<Recurso, Long> {
}
