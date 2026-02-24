package com.example.gestiontareas.services;

import java.util.List;
import com.example.gestiontareas.model.TareaRecurso;

public interface TareaRecursoService {

    void asignarRecurso(Long tareaId, Long recursoId, int cantidad);

    void eliminarAsignacion(Long tareaRecursoId);

    void liberarRecursosDeTarea(Long tareaId);

    List<TareaRecurso> listarPorTarea(Long tareaId);
}