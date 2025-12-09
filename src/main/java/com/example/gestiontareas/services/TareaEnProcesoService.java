package com.example.gestiontareas.services;

import java.util.List;

import com.example.gestiontareas.dto.Request.TareaRequest;
import com.example.gestiontareas.dto.Response.TareaResponse;

public interface TareaEnProcesoService {
    TareaResponse create(TareaRequest request);
    List<TareaResponse> listByUsuario(Long usuarioId);
    TareaResponse getById(Long id);
    void delete(Long id);
}
