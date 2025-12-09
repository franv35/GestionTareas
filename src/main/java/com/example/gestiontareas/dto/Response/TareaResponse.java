package com.example.gestiontareas.dto.Response;

import java.util.List;

public class TareaResponse {
    private Long id;
    private String titulo;
    private String descripcion;
    private String fecha;   // ahora es String, igual que en la entidad Tarea
    private Long usuarioId;

    // Recursos asociados a la tarea
    private List<RecursoResponse> recursos;

    // getters y setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

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

    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<RecursoResponse> getRecursos() {
        return recursos;
    }
    public void setRecursos(List<RecursoResponse> recursos) {
        this.recursos = recursos;
    }
}
