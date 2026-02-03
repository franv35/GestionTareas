package com.example.gestiontareas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestiontareas.model.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByProyectoId(Long proyectoId);
}
