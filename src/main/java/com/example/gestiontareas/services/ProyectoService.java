package com.example.gestiontareas.services;

import com.example.gestiontareas.dto.Request.ProyectoRequest;
import com.example.gestiontareas.dto.Response.ProyectoResponse;

import java.util.List;
import java.util.Map;

public interface ProyectoService {

    /**
     * Crear un nuevo proyecto
     */
    ProyectoResponse crearProyecto(ProyectoRequest request);

    /**
     * Obtener un proyecto por ID
     */
    ProyectoResponse obtenerProyectoPorId(Long id);

    /**
     * Obtener todos los proyectos
     */
    List<ProyectoResponse> obtenerTodosLosProyectos();

    /**
     * Obtener proyectos por estado
     */
    List<ProyectoResponse> obtenerProyectosPorEstado(String estado);

    /**
     * Buscar proyectos por nombre
     */
    List<ProyectoResponse> buscarProyectosPorNombre(String nombre);

    /**
     * Actualizar un proyecto
     */
    ProyectoResponse actualizarProyecto(Long id, ProyectoRequest request);

    /**
     * Actualizar contadores de tareas de un proyecto
     */
    ProyectoResponse actualizarContadoresTareas(Long id, Integer pendientes, Integer enProceso, Integer terminadas);

    /**
     * Eliminar un proyecto
     */
    void eliminarProyecto(Long id);

    /**
     * Cambiar estado de un proyecto
     */
    ProyectoResponse cambiarEstadoProyecto(Long id, String nuevoEstado);

    /**
     * Obtener estadísticas generales de todos los proyectos
     */
    Map<String, Object> obtenerEstadisticasGenerales();
}
