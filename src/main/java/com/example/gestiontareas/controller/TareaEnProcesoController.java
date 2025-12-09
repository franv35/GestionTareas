package com.example.gestiontareas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestiontareas.dto.Request.TareaRequest;
import com.example.gestiontareas.dto.Response.TareaResponse;
import com.example.gestiontareas.services.TareaEnProcesoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tareas-en-proceso")
public class TareaEnProcesoController {

    private final TareaEnProcesoService tareaEnProcesoService;

    public TareaEnProcesoController(TareaEnProcesoService tareaEnProcesoService) {
        this.tareaEnProcesoService = tareaEnProcesoService;
    }

    @PostMapping
    public ResponseEntity<TareaResponse> create(@Valid @RequestBody TareaRequest req) {
        return ResponseEntity.ok(tareaEnProcesoService.create(req));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TareaResponse>> listByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(tareaEnProcesoService.listByUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tareaEnProcesoService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tareaEnProcesoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
