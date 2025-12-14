package com.example.gestiontareas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.gestiontareas.dto.Request.TareaRequest;
import com.example.gestiontareas.dto.Response.TareaResponse;
import com.example.gestiontareas.services.TareaEnProcesoService;

@RestController
@RequestMapping("/api/tareas-en-proceso")
@CrossOrigin(origins = "*")
public class TareaEnProcesoController {

    @Autowired
    private TareaEnProcesoService tareaEnProcesoService;

    @PostMapping
    public ResponseEntity<TareaResponse> create(@RequestBody TareaRequest request) {
        TareaResponse response = tareaEnProcesoService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TareaResponse>> listByUsuario(@PathVariable Long usuarioId) {
        List<TareaResponse> tareas = tareaEnProcesoService.listByUsuario(usuarioId);
        return ResponseEntity.ok(tareas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaResponse> getById(@PathVariable Long id) {
        TareaResponse response = tareaEnProcesoService.getById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tareaEnProcesoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/mover/{id}")
    public ResponseEntity<TareaResponse> moverATerminada(@PathVariable Long id) {
        TareaResponse response = tareaEnProcesoService.moverATerminada(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/mover-desde-terminadas/{id}")
    public ResponseEntity<TareaResponse> moverDesdeTerminada(@PathVariable Long id) {
        TareaResponse response = tareaEnProcesoService.moverDesdeTerminada(id);
        return ResponseEntity.ok(response);
    }
}
