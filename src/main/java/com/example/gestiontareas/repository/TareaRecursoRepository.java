package com.example.gestiontareas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestiontareas.model.TareaRecurso;

public interface TareaRecursoRepository extends JpaRepository<TareaRecurso, Long> {

    List<TareaRecurso> findByTareaId(Long tareaId);

    List<TareaRecurso> findByRecursoId(Long recursoId);

	boolean existsByRecursoId(Long recursoId);
}