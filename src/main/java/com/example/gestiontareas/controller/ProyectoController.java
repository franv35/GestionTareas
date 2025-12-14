package com.example.gestiontareas.controller;

import java.util.List;

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
import com.example.gestiontareas.model.Proyecto;
import com.example.gestiontareas.model.Tarea;
import com.example.gestiontareas.services.ProyectoService;

@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "*")
public class ProyectoController {

    @Autowired
    private ProyectoService proyectoService;

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Proyecto> crearProyecto(
            @PathVariable Long usuarioId,
            @RequestBody Proyecto proyecto) {

        return ResponseEntity.ok(proyectoService.crearProyecto(usuarioId, proyecto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> obtenerProyecto(@PathVariable Long id) {
        return ResponseEntity.ok(proyectoService.obtenerProyecto(id));
    }

    @GetMapping
    public ResponseEntity<List<Proyecto>> listarProyectos() {
        return ResponseEntity.ok(proyectoService.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proyecto> editarProyecto(
            @PathVariable Long id,
            @RequestBody Proyecto proyecto) {

        return ResponseEntity.ok(proyectoService.editarProyecto(id, proyecto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProyecto(@PathVariable Long id) {
        proyectoService.eliminarProyecto(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/tareas")
    public ResponseEntity<List<Tarea>> obtenerTareas(@PathVariable Long id) {
        return ResponseEntity.ok(proyectoService.obtenerTareas(id));
    }
}
