package com.example.gestiontareas.dto.Response;

import java.util.List;

public class ProyectoResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private Long usuarioId;
    private List<TareaResponse> tareas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<TareaResponse> getTareas() {
        return tareas;
    }

    public void setTareas(List<TareaResponse> tareas) {
        this.tareas = tareas;
    }
}
