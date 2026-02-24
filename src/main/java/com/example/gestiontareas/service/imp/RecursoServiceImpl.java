package com.example.gestiontareas.service.imp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.gestiontareas.Mapper.AppMapper;
import com.example.gestiontareas.dto.Request.RecursoRequest;
import com.example.gestiontareas.dto.Response.RecursoResponse;
import com.example.gestiontareas.model.Recurso;
import com.example.gestiontareas.repository.RecursoRepository;
import com.example.gestiontareas.repository.TareaRecursoRepository;
import com.example.gestiontareas.services.RecursoService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RecursoServiceImpl implements RecursoService {

    private final RecursoRepository recursoRepository;
    private final TareaRecursoRepository tareaRecursoRepository;

    public RecursoServiceImpl(
            RecursoRepository recursoRepository,
            TareaRecursoRepository tareaRecursoRepository) {

        this.recursoRepository = recursoRepository;
        this.tareaRecursoRepository = tareaRecursoRepository;
    }

    @Override
    public RecursoResponse create(RecursoRequest req) {

        Recurso recurso = new Recurso();

        recurso.setNombre(req.getNombre());

        // cantidad del request se transforma en stockTotal
        recurso.setStockTotal(req.getCantidad());

        // al crearse, disponible = total
        recurso.setStockDisponible(req.getCantidad());

        recurso.setUnidad(req.getUnidad());

        Recurso saved = recursoRepository.save(recurso);

        return AppMapper.toRecursoResponse(saved);
    }

    @Override
    public List<RecursoResponse> listAll() {
        return recursoRepository.findAll()
                .stream()
                .map(AppMapper::toRecursoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RecursoResponse getById(Long id) {
        Recurso recurso = recursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado"));

        return AppMapper.toRecursoResponse(recurso);
    }

    @Override
    public RecursoResponse update(Long id, RecursoRequest req) {

        Recurso recurso = recursoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Recurso no encontrado"));

        recurso.setNombre(req.getNombre());

        recurso.setStockTotal(req.getCantidad());

        // ⚠️ cuidado acá:
        recurso.setStockDisponible(req.getCantidad());

        recurso.setUnidad(req.getUnidad());

        Recurso updated = recursoRepository.save(recurso);

        return AppMapper.toRecursoResponse(updated);
    }

    @Override
    public void delete(Long id) {

        if (tareaRecursoRepository.existsByRecursoId(id)) {
            throw new IllegalStateException(
                "No se puede eliminar el recurso porque está asignado a tareas");
        }

        recursoRepository.deleteById(id);
    }
}