package com.example.gestiontareas.services;

import com.example.gestiontareas.dto.Request.UsuarioLoginRequest;
import com.example.gestiontareas.dto.Request.UsuarioRegisterRequestDto;
import com.example.gestiontareas.dto.Response.UsuarioResponse;
import com.example.gestiontareas.model.Usuario;

public interface UsuarioService {

	UsuarioResponse register(UsuarioRegisterRequestDto request);
    UsuarioResponse login(UsuarioLoginRequest request); // para MVP básico
    UsuarioResponse findById(Long id);
	Usuario findByEmail(String email);
}
