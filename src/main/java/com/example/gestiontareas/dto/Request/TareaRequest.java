package com.example.gestiontareas.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class TareaRequest {

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 255, message = "El título no puede superar los 255 caracteres")
    private String titulo;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El usuario es obligatorio")
    private Long usuarioId;

    // Relación con recursos (antes era categoriaId)
    private List<Long> recursoIds;

    // getters y setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<Long> getRecursoIds() {
        return recursoIds;
    }

    public void setRecursoIds(List<Long> recursoIds) {
        this.recursoIds = recursoIds;
    }
}
