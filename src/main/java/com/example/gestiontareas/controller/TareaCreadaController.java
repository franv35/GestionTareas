package com.example.gestiontareas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.gestiontareas.dto.Request.TareaRequest;
import com.example.gestiontareas.dto.Response.TareaResponse;
import com.example.gestiontareas.services.TareaCreadaService;

@RestController
@RequestMapping("/api/tareas-creadas")
@CrossOrigin(origins = "*")
public class TareaCreadaController {

    @Autowired
    private TareaCreadaService tareaCreadaService;

    @PostMapping
    public ResponseEntity<TareaResponse> create(@RequestBody TareaRequest request) {
        TareaResponse response = tareaCreadaService.create(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<TareaResponse>> listByUsuario(@PathVariable Long usuarioId) {
        List<TareaResponse> tareas = tareaCreadaService.listByUsuario(usuarioId);
        return ResponseEntity.ok(tareas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaResponse> getById(@PathVariable Long id) {
        TareaResponse response = tareaCreadaService.getById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tareaCreadaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/mover/{id}")
    public ResponseEntity<TareaResponse> moverAEnProceso(@PathVariable Long id) {
        TareaResponse response = tareaCreadaService.moverAEnProceso(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/mover-desde-proceso/{id}")
    public ResponseEntity<TareaResponse> moverDesdeEnProceso(@PathVariable Long id) {
        TareaResponse response = tareaCreadaService.moverDesdeEnProceso(id);
        return ResponseEntity.ok(response);
    }
}
