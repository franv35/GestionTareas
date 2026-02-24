package com.example.gestiontareas.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoTarea estado = EstadoTarea.PENDIENTE;

    /* ================= RELACIÓN CON PROYECTO ================= */

    @ManyToOne
    @JoinColumn(name = "proyecto_id", nullable = false)
    @JsonBackReference
    private Proyecto proyecto;

    /* ================= RELACIÓN CON TAREA_RECURSO ================= */

    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TareaRecurso> asignaciones = new ArrayList<>();


    /* ================= GETTERS & SETTERS ================= */

    public Long getId() {
        return id;
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

    public EstadoTarea getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public List<TareaRecurso> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<TareaRecurso> asignaciones) {
        this.asignaciones = asignaciones;
    }

    /* ================= MÉTODOS DE APOYO ================= */

    public void agregarAsignacion(TareaRecurso asignacion) {
        asignaciones.add(asignacion);
        asignacion.setTarea(this);
    }

    public void removerAsignacion(TareaRecurso asignacion) {
        asignaciones.remove(asignacion);
        asignacion.setTarea(null);
    }
}