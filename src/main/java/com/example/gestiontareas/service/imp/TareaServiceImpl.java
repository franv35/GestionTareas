package com.example.gestiontareas.service.imp;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gestiontareas.model.EstadoTarea;
import com.example.gestiontareas.model.Proyecto;
import com.example.gestiontareas.model.Tarea;
import com.example.gestiontareas.repository.ProyectoRepository;
import com.example.gestiontareas.repository.TareaRepository;
import com.example.gestiontareas.services.TareaRecursoService;
import com.example.gestiontareas.services.TareaService;

@Service
public class TareaServiceImpl implements TareaService {

    private final TareaRepository tareaRepository;
    private final ProyectoRepository proyectoRepository;
    private final TareaRecursoService tareaRecursoService;

    public TareaServiceImpl(
            TareaRepository tareaRepository,
            ProyectoRepository proyectoRepository,
            TareaRecursoService tareaRecursoService) {

        this.tareaRepository = tareaRepository;
        this.proyectoRepository = proyectoRepository;
        this.tareaRecursoService = tareaRecursoService;
    }

    /* ======================================================
       CREAR TAREA
    ====================================================== */

    @Override
    public Tarea crearTarea(Long proyectoId, Tarea tarea) {

        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        tarea.setProyecto(proyecto);

        return tareaRepository.save(tarea);
    }

    /* ======================================================
       EDITAR TAREA
    ====================================================== */

    @Override
    public Tarea editarTarea(Long tareaId, Tarea tareaActualizada) {

        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        tarea.setTitulo(tareaActualizada.getTitulo());
        tarea.setDescripcion(tareaActualizada.getDescripcion());
        tarea.setEstado(tareaActualizada.getEstado());

        return tareaRepository.save(tarea);
    }

    /* ======================================================
       ELIMINAR TAREA (LIBERA STOCK)
    ====================================================== */

    @Override
    @Transactional
    public void eliminarTarea(Long id) {

        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        // 🔥 Liberar recursos antes de eliminar
        tareaRecursoService.liberarRecursosDeTarea(id);

        tareaRepository.delete(tarea);
    }

    /* ======================================================
       OBTENER TAREA
    ====================================================== */

    @Override
    public Tarea obtenerTarea(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    }

    /* ======================================================
       CAMBIAR ESTADO
    ====================================================== */

    @Override
    public Tarea cambiarEstado(Long tareaId, EstadoTarea estado) {

        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        tarea.setEstado(estado);

        return tareaRepository.save(tarea);
    }
}