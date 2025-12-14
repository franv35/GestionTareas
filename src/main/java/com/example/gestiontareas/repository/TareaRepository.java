package com.example.gestiontareas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestiontareas.model.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
}
