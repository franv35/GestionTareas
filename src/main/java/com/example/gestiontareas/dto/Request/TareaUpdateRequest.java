package com.example.gestiontareas.dto.Request;

import com.example.gestiontareas.model.EstadoTarea;
import java.time.LocalDate;
import java.util.List;

public class TareaUpdateRequest {

    private String titulo;
    private String descripcion;
    private LocalDate fecha;
    private EstadoTarea estado;
    private List<RecursoRequest> recursos;

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

    public List<RecursoRequest> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<RecursoRequest> recursos) {
        this.recursos = recursos;
    }
}
