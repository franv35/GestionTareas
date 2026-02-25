package com.example.gestiontareas.service.imp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.gestiontareas.Mapper.AppMapper;
import com.example.gestiontareas.dto.Request.RecursoRequest;
import com.example.gestiontareas.dto.Response.RecursoResponse;
import com.example.gestiontareas.model.Proyecto;
import com.example.gestiontareas.model.Recurso;
import com.example.gestiontareas.repository.ProyectoRepository;
import com.example.gestiontareas.repository.RecursoRepository;
import com.example.gestiontareas.repository.TareaRecursoRepository;
import com.example.gestiontareas.services.RecursoService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RecursoServiceImpl implements RecursoService {

    private final RecursoRepository recursoRepository;
    private final TareaRecursoRepository tareaRecursoRepository;
    private final ProyectoRepository proyectoRepository;

    public RecursoServiceImpl(
            RecursoRepository recursoRepository,
            TareaRecursoRepository tareaRecursoRepository,
            ProyectoRepository proyectoRepository) {

        this.recursoRepository = recursoRepository;
        this.tareaRecursoRepository = tareaRecursoRepository;
        this.proyectoRepository = proyectoRepository;
    }

    /* =========================
       CREATE
       ========================= */
    @Override
    public RecursoResponse create(RecursoRequest req) {

        Proyecto proyecto = proyectoRepository.findById(req.getProyectoId())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        Recurso recurso = new Recurso();
        recurso.setNombre(req.getNombre());
        recurso.setStockTotal(req.getStockTotal());
        recurso.setStockDisponible(req.getStockTotal()); // 🔥 al crearse
        recurso.setUnidad(req.getUnidad());
        recurso.setProyecto(proyecto); // 🔥 CLAVE

        Recurso saved = recursoRepository.save(recurso);

        return AppMapper.toRecursoResponse(saved);
    }

    /* =========================
       LIST ALL
       ========================= */
    @Override
    public List<RecursoResponse> listAll() {
        return recursoRepository.findAll()
                .stream()
                .map(AppMapper::toRecursoResponse)
                .collect(Collectors.toList());
    }

    /* =========================
       GET BY ID
       ========================= */
    @Override
    public RecursoResponse getById(Long id) {
        Recurso recurso = recursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado"));

        return AppMapper.toRecursoResponse(recurso);
    }

    /* =========================
       UPDATE
       ========================= */
    @Override
    public RecursoResponse update(Long id, RecursoRequest req) {

        Recurso recurso = recursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado"));

        recurso.setNombre(req.getNombre());
        recurso.setUnidad(req.getUnidad());

        // 🔥 Solo actualizamos stockTotal.
        // No tocamos stockDisponible automáticamente.
        recurso.setStockTotal(req.getStockTotal());

        Recurso updated = recursoRepository.save(recurso);

        return AppMapper.toRecursoResponse(updated);
    }

    /* =========================
       DELETE
       ========================= */
    @Override
    public void delete(Long id) {

        if (tareaRecursoRepository.existsByRecursoId(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar el recurso porque está asignado a tareas");
        }

        recursoRepository.deleteById(id);
    }
}