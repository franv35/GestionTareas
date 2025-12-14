package com.example.gestiontareas.controller;

import com.example.gestiontareas.dto.Request.ProyectoRequest;
import com.example.gestiontareas.dto.Response.ProyectoResponse;
import com.example.gestiontareas.services.ProyectoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProyectoController {

    private final ProyectoService proyectoService;

    /**
     * Crear un nuevo proyecto
     * POST /api/proyectos
     */
    @PostMapping
    public ResponseEntity<ProyectoResponse> crearProyecto(@Valid @RequestBody ProyectoRequest request) {
        try {
            ProyectoResponse response = proyectoService.crearProyecto(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Obtener todos los proyectos
     * GET /api/proyectos
     */
    @GetMapping
    public ResponseEntity<List<ProyectoResponse>> obtenerTodosLosProyectos() {
        try {
            List<ProyectoResponse> proyectos = proyectoService.obtenerTodosLosProyectos();
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener un proyecto por ID
     * GET /api/proyectos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponse> obtenerProyectoPorId(@PathVariable Long id) {
        try {
            ProyectoResponse proyecto = proyectoService.obtenerProyectoPorId(id);
            return ResponseEntity.ok(proyecto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Obtener proyectos por estado
     * GET /api/proyectos/estado/{estado}
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ProyectoResponse>> obtenerProyectosPorEstado(@PathVariable String estado) {
        try {
            List<ProyectoResponse> proyectos = proyectoService.obtenerProyectosPorEstado(estado);
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Buscar proyectos por nombre
     * GET /api/proyectos/buscar?nombre=texto
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ProyectoResponse>> buscarProyectosPorNombre(@RequestParam String nombre) {
        try {
            List<ProyectoResponse> proyectos = proyectoService.buscarProyectosPorNombre(nombre);
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Actualizar un proyecto
     * PUT /api/proyectos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProyectoResponse> actualizarProyecto(
            @PathVariable Long id,
            @Valid @RequestBody ProyectoRequest request) {
        try {
            ProyectoResponse response = proyectoService.actualizarProyecto(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Actualizar contadores de tareas
     * PATCH /api/proyectos/{id}/contadores
     */
    @PatchMapping("/{id}/contadores")
    public ResponseEntity<ProyectoResponse> actualizarContadoresTareas(
            @PathVariable Long id,
            @RequestParam(required = false) Integer pendientes,
            @RequestParam(required = false) Integer enProceso,
            @RequestParam(required = false) Integer terminadas) {
        try {
            ProyectoResponse response = proyectoService.actualizarContadoresTareas(
                    id, pendientes, enProceso, terminadas);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Cambiar estado de un proyecto
     * PATCH /api/proyectos/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ProyectoResponse> cambiarEstadoProyecto(
            @PathVariable Long id,
            @RequestParam String estado) {
        try {
            ProyectoResponse response = proyectoService.cambiarEstadoProyecto(id, estado);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Eliminar un proyecto
     * DELETE /api/proyectos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProyecto(@PathVariable Long id) {
        try {
            proyectoService.eliminarProyecto(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Obtener estadísticas generales
     * GET /api/proyectos/estadisticas/generales
     */
    @GetMapping("/estadisticas/generales")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasGenerales() {
        try {
            Map<String, Object> estadisticas = proyectoService.obtenerEstadisticasGenerales();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
