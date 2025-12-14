package com.example.gestiontareas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestiontareas.dto.Request.CambiarEstadoTareaRequest;
import com.example.gestiontareas.model.Tarea;
import com.example.gestiontareas.services.TareaService;

@RestController
@RequestMapping("/api/tareas")
@CrossOrigin(origins = "*")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @PostMapping("/proyecto/{proyectoId}")
    public ResponseEntity<Tarea> crearTarea(
            @PathVariable Long proyectoId,
            @RequestBody Tarea tarea) {

        return ResponseEntity.ok(tareaService.crearTarea(proyectoId, tarea));
    }

    @PutMapping("/{tareaId}")
    public ResponseEntity<Tarea> editarTarea(
            @PathVariable Long tareaId,
            @RequestBody Tarea tarea) {

        return ResponseEntity.ok(tareaService.editarTarea(tareaId, tarea));
    }
    
    @PutMapping("/{id}/estado")
    public ResponseEntity<Tarea> cambiarEstado(
            @PathVariable Long id,
            @RequestBody CambiarEstadoTareaRequest req) {

        return ResponseEntity.ok(
            tareaService.cambiarEstado(id, req.getEstado())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTarea(@PathVariable Long id) {
        tareaService.eliminarTarea(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarea> obtenerTarea(@PathVariable Long id) {
        return ResponseEntity.ok(tareaService.obtenerTarea(id));
    }
    
    @PostMapping("/{tareaId}/recursos/{recursoId}")
    public ResponseEntity<Tarea> asignarRecurso(
            @PathVariable Long tareaId,
            @PathVariable Long recursoId) {

        return ResponseEntity.ok(
            tareaService.asignarRecurso(tareaId, recursoId)
        );
    }
}
