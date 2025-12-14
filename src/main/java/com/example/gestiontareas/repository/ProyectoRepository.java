package com.example.gestiontareas.repository;


import com.example.gestiontareas.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
 List<Proyecto> findByUsuarioId(Long usuarioId);
}
