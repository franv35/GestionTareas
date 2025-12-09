package com.example.gestiontareas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestiontareas.model.TareaEnProceso;
import com.example.gestiontareas.model.Usuario;

import java.util.List;

public interface TareaEnProcesoRepository extends JpaRepository<TareaEnProceso, Long> {
    List<TareaEnProceso> findByUsuarioOrderByFechaDesc(Usuario usuario);
}
