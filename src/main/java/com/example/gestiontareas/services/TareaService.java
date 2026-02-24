package com.example.gestiontareas.services;

import com.example.gestiontareas.model.EstadoTarea;
import com.example.gestiontareas.model.Tarea;

public interface TareaService {

    Tarea crearTarea(Long proyectoId, Tarea tarea);

    Tarea editarTarea(Long tareaId, Tarea tarea);

    void eliminarTarea(Long id);

    Tarea obtenerTarea(Long id);

    Tarea cambiarEstado(Long tareaId, EstadoTarea estado);
}