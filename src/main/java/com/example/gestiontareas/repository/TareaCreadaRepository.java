package com.example.gestiontareas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestiontareas.model.TareaCreada;
import com.example.gestiontareas.model.Usuario;

import java.util.List;

public interface TareaCreadaRepository extends JpaRepository<TareaCreada, Long> {
    List<TareaCreada> findByUsuarioOrderByFechaDesc(Usuario usuario);
}
