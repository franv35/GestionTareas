package com.example.gestiontareas.repository;

import com.example.gestiontareas.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    // Buscar proyectos por estado
    List<Proyecto> findByEstado(String estado);

    // Buscar proyectos por nombre (búsqueda parcial)
    List<Proyecto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar proyectos activos
    @Query("SELECT p FROM Proyecto p WHERE p.estado = 'ACTIVO' ORDER BY p.fechaCreacion DESC")
    List<Proyecto> findProyectosActivos();

    // Contar proyectos por estado
    Long countByEstado(String estado);

    // Obtener estadísticas generales
    @Query("SELECT SUM(p.tareasEnProceso) FROM Proyecto p")
    Long sumTareasEnProceso();

    @Query("SELECT SUM(p.tareasTerminadas) FROM Proyecto p")
    Long sumTareasTerminadas();

    @Query("SELECT SUM(p.tareasPendientes) FROM Proyecto p")
    Long sumTareasPendientes();
}


