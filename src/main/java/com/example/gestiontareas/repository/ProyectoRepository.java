package com.example.gestiontareas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestiontareas.model.Proyecto;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    List<Proyecto> findByUsuarioId(Long usuarioId);
}
