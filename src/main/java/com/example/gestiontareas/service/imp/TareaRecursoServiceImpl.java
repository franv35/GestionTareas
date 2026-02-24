package com.example.gestiontareas.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gestiontareas.model.Recurso;
import com.example.gestiontareas.model.Tarea;
import com.example.gestiontareas.model.TareaRecurso;
import com.example.gestiontareas.repository.RecursoRepository;
import com.example.gestiontareas.repository.TareaRecursoRepository;
import com.example.gestiontareas.repository.TareaRepository;
import com.example.gestiontareas.services.TareaRecursoService;
import com.example.gestiontareas.model.EstadoTarea;

@Service
public class TareaRecursoServiceImpl implements TareaRecursoService {

    private final TareaRecursoRepository tareaRecursoRepository;
    private final TareaRepository tareaRepository;
    private final RecursoRepository recursoRepository;

    public TareaRecursoServiceImpl(
            TareaRecursoRepository tareaRecursoRepository,
            TareaRepository tareaRepository,
            RecursoRepository recursoRepository) {

        this.tareaRecursoRepository = tareaRecursoRepository;
        this.tareaRepository = tareaRepository;
        this.recursoRepository = recursoRepository;
    }

    /* ======================================================
       ASIGNAR RECURSO A TAREA
    ====================================================== */

    @Override
    @Transactional
    public void asignarRecurso(Long tareaId, Long recursoId, int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        // 🔥 VALIDACIÓN DE ESTADO
        if (tarea.getEstado() == EstadoTarea.COMPLETADA) {
            throw new IllegalStateException("No se pueden asignar recursos a una tarea finalizada");
        }

        Recurso recurso = recursoRepository.findById(recursoId)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado"));

        // Validación de coherencia de proyecto
        if (!recurso.getProyecto().getId().equals(tarea.getProyecto().getId())) {
            throw new IllegalStateException("El recurso no pertenece al mismo proyecto que la tarea");
        }

        if (recurso.getStockDisponible() < cantidad) {
            throw new IllegalStateException("Stock insuficiente");
        }

        // Descontar stock
        recurso.setStockDisponible(
                recurso.getStockDisponible() - cantidad
        );

        TareaRecurso asignacion = new TareaRecurso();
        asignacion.setTarea(tarea);
        asignacion.setRecurso(recurso);
        asignacion.setCantidadAsignada(cantidad);

        tareaRecursoRepository.save(asignacion);
    }

    /* ======================================================
       ELIMINAR ASIGNACIÓN (DEVUELVE STOCK)
    ====================================================== */

    @Override
    @Transactional
    public void eliminarAsignacion(Long asignacionId) {

        TareaRecurso asignacion = tareaRecursoRepository.findById(asignacionId)
            .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

     // 🔒 VALIDACIÓN
        if (asignacion.getTarea().getEstado() == EstadoTarea.COMPLETADA) {
            throw new IllegalStateException(
                "No se pueden modificar recursos de una tarea finalizada"
            );
        }

        // Devolver stock
        Recurso recurso = asignacion.getRecurso();

        recurso.setStockDisponible(
            recurso.getStockDisponible() + asignacion.getCantidadAsignada()
        );

        recursoRepository.save(recurso);

        tareaRecursoRepository.delete(asignacion);
    }

    /* ======================================================
       LIBERAR TODOS LOS RECURSOS DE UNA TAREA
    ====================================================== */

    @Override
    @Transactional
    public void liberarRecursosDeTarea(Long tareaId) {

        List<TareaRecurso> asignaciones =
                tareaRecursoRepository.findByTareaId(tareaId);

        for (TareaRecurso asignacion : asignaciones) {

            Recurso recurso = asignacion.getRecurso();

            recurso.setStockDisponible(
                    recurso.getStockDisponible() + asignacion.getCantidadAsignada()
            );

            tareaRecursoRepository.delete(asignacion);
        }
    }
}