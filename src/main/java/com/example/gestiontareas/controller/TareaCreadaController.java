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
import com.example.gestiontareas.services.TareaCreadaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tareas-creadas")
public class TareaCreadaController {
    
    private final TareaCreadaService tareaCreadaService;

    public TareaCreadaController(TareaCreadaService tareaCreadaService) {
        this.tareaCreadaService = tareaCreadaService;
    }

    @PostMapping
    public ResponseEntity<TareaResponse> create(@Valid @RequestBody TareaRequest req) {
        return ResponseEntity.ok(tareaCreadaService.create(req));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TareaResponse>> listByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(tareaCreadaService.listByUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tareaCreadaService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tareaCreadaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
