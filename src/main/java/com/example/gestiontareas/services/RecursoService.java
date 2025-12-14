package com.example.gestiontareas.services;

import java.util.List;

import com.example.gestiontareas.dto.Request.RecursoRequest;
import com.example.gestiontareas.dto.Response.RecursoResponse;

public interface RecursoService {

    RecursoResponse create(RecursoRequest req);

    List<RecursoResponse> listAll();

    RecursoResponse getById(Long id);
}
