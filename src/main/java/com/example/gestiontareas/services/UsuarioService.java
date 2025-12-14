package com.example.gestiontareas.services;

import java.util.List;

import com.example.gestiontareas.dto.Request.UsuarioLoginRequest;
import com.example.gestiontareas.dto.Request.UsuarioRegisterRequest;
import com.example.gestiontareas.dto.Response.UsuarioResponse;
import com.example.gestiontareas.model.Proyecto;
import com.example.gestiontareas.model.Usuario;

public interface UsuarioService {

    UsuarioResponse register(UsuarioRegisterRequest request);

    UsuarioResponse login(UsuarioLoginRequest request);

    UsuarioResponse findById(Long id);

    Usuario findByEmail(String email);

    List<Proyecto> obtenerProyectos(Long usuarioId);
}
