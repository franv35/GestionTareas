package com.example.gestiontareas.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.gestiontareas.dto.Response.AsignarRecursoDTO;
import com.example.gestiontareas.model.TareaRecurso;
import com.example.gestiontareas.services.TareaRecursoService;

@RestController
@RequestMapping("/api/tareas-recursos")
public class TareaRecursoController {

    private final TareaRecursoService tareaRecursoService;

    public TareaRecursoController(TareaRecursoService tareaRecursoService) {
        this.tareaRecursoService = tareaRecursoService;
    }

    /* ======================================================
       ASIGNAR RECURSO A TAREA
    ====================================================== */

    @PostMapping
    public ResponseEntity<String> asignarRecurso(
            @RequestBody AsignarRecursoDTO dto) {

        tareaRecursoService.asignarRecurso(
                dto.getTareaId(),
                dto.getRecursoId(),
                dto.getCantidad()
        );

        return ResponseEntity.ok("Recurso asignado correctamente");
    }

    /* ======================================================
       ELIMINAR ASIGNACIÓN
    ====================================================== */

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarAsignacion(
            @PathVariable Long id) {

        tareaRecursoService.eliminarAsignacion(id);

        return ResponseEntity.ok("Asignación eliminada correctamente");
    }

    /* ======================================================
       LISTAR ASIGNACIONES DE UNA TAREA
    ====================================================== */

    @GetMapping("/tarea/{tareaId}")
    public ResponseEntity<List<TareaRecurso>> listarPorTarea(
            @PathVariable Long tareaId) {

        return ResponseEntity.ok(
                tareaRecursoService.listarPorTarea(tareaId)
        );
    }
}