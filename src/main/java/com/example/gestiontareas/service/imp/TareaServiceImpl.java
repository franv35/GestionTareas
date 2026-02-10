package com.example.gestiontareas.service.imp;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.gestiontareas.model.EstadoTarea;
import com.example.gestiontareas.model.Proyecto;
import com.example.gestiontareas.model.Recurso;
import com.example.gestiontareas.model.Tarea;
import com.example.gestiontareas.repository.ProyectoRepository;
import com.example.gestiontareas.repository.RecursoRepository;
import com.example.gestiontareas.repository.TareaRepository;
import com.example.gestiontareas.services.TareaService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TareaServiceImpl implements TareaService {

    private final TareaRepository tareaRepository;
    private final ProyectoRepository proyectoRepository;
    private final RecursoRepository recursoRepository;

    public TareaServiceImpl(
            TareaRepository tareaRepository,
            ProyectoRepository proyectoRepository,
            RecursoRepository recursoRepository) {

        this.tareaRepository = tareaRepository;
        this.proyectoRepository = proyectoRepository;
        this.recursoRepository = recursoRepository;
    }

    @Override
    public Tarea crearTarea(Long proyectoId, Tarea tarea) {
    	if (tarea.getEstado() == null) {
            tarea.setEstado(EstadoTarea.PENDIENTE);
        }
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));

        tarea.setProyecto(proyecto);

        return tareaRepository.save(tarea);
    }

    @Override
    public Tarea editarTarea(Long tareaId, Tarea tarea) {
        Tarea existente = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));

        existente.setTitulo(tarea.getTitulo());
        existente.setDescripcion(tarea.getDescripcion());
        existente.setEstado(tarea.getEstado());
        existente.setRecursos(tarea.getRecursos());

        return tareaRepository.save(existente);
    }

    @Override
    public Tarea cambiarEstado(Long tareaId, EstadoTarea estado) {
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));

        tarea.setEstado(estado);
        return tareaRepository.save(tarea);
    }
    
    @Override
    public void eliminarTarea(Long id) {
        if (!tareaRepository.existsById(id)) {
            throw new IllegalArgumentException("Tarea no encontrada");
        }
        tareaRepository.deleteById(id);
    }

    @Override
    public Tarea obtenerTarea(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
    }
    
    @Override
    public Tarea asignarRecurso(Long tareaId, Long recursoId) {
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        Recurso recurso = recursoRepository.findById(recursoId)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado"));

        tarea.getRecursos().add(recurso);
        return tareaRepository.save(tarea);
    }

}
