package com.example.gestiontareas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestiontareas.model.TareaTerminada;
import com.example.gestiontareas.model.Usuario;

import java.util.List;

public interface TareaTerminadaRepository extends JpaRepository<TareaTerminada, Long> {
    List<TareaTerminada> findByUsuarioOrderByFechaDesc(Usuario usuario);
}
