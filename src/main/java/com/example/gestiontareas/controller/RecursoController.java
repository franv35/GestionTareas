package com.example.gestiontareas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestiontareas.dto.Request.RecursoRequest;
import com.example.gestiontareas.dto.Response.RecursoResponse;
import com.example.gestiontareas.services.RecursoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

    private final RecursoService recursoService;

    public RecursoController(RecursoService recursoService) {
        this.recursoService = recursoService;
    }

    @PostMapping
    public ResponseEntity<RecursoResponse> create(@Valid @RequestBody RecursoRequest req) {
        return ResponseEntity.ok(recursoService.create(req));
    }

    @GetMapping
    public ResponseEntity<List<RecursoResponse>> listAll() {
        return ResponseEntity.ok(recursoService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(recursoService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecursoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody RecursoRequest req) {

        return ResponseEntity.ok(recursoService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}