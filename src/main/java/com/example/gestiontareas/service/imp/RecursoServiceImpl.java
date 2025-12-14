package com.example.gestiontareas.service.imp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.gestiontareas.Mapper.AppMapper;
import com.example.gestiontareas.dto.Request.RecursoRequest;
import com.example.gestiontareas.dto.Response.RecursoResponse;
import com.example.gestiontareas.model.Recurso;
import com.example.gestiontareas.repository.RecursoRepository;
import com.example.gestiontareas.services.RecursoService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RecursoServiceImpl implements RecursoService {

    private final RecursoRepository recursoRepository;

    public RecursoServiceImpl(RecursoRepository recursoRepository) {
        this.recursoRepository = recursoRepository;
    }

    @Override
    public RecursoResponse create(RecursoRequest req) {
        Recurso recurso = new Recurso();
        recurso.setNombre(req.getNombre());
        recurso.setCantidad(req.getCantidad());
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
}
