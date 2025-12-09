package com.example.gestiontareas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestiontareas.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}