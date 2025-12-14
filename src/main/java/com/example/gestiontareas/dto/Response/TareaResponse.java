package com.example.gestiontareas.dto.Response;

import java.time.LocalDate;
import java.util.List;

import com.example.gestiontareas.model.EstadoTarea;

public class TareaResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDate fecha;
    private EstadoTarea estado;
    private Long proyectoId;
    private List<RecursoResponse> recursos;

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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }

    public Long getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Long proyectoId) {
        this.proyectoId = proyectoId;
    }

    public List<RecursoResponse> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<RecursoResponse> recursos) {
        this.recursos = recursos;
    }
}
