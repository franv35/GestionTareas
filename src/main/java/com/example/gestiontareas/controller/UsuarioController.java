package com.example.gestiontareas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.gestiontareas.dto.Request.UsuarioLoginRequest;
import com.example.gestiontareas.dto.Request.UsuarioRegisterRequest;
import com.example.gestiontareas.dto.Response.UsuarioResponse;
import com.example.gestiontareas.model.Proyecto;
import com.example.gestiontareas.services.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registrar(
            @RequestBody UsuarioRegisterRequest request) {

        return ResponseEntity.ok(usuarioService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(
            @RequestBody @Valid UsuarioLoginRequest req) {

        UsuarioResponse response = usuarioService.login(req);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}/proyectos")
    public ResponseEntity<List<Proyecto>> obtenerProyectos(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerProyectos(id));
    }
}
