package com.example.gestiontareas.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tarea_recurso")
public class TareaRecurso {

    /* =========================
       ID
       ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* =========================
       CANTIDAD ASIGNADA
       ========================= */
    @Column(nullable = false)
    private int cantidadAsignada;

    /* =========================
       RELACIÓN CON TAREA
       Muchas asignaciones → Una tarea
       ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarea_id", nullable = false)
    @JsonIgnore
    private Tarea tarea;

    /* =========================
       RELACIÓN CON RECURSO
       Muchas asignaciones → Un recurso
       ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurso_id", nullable = false)
    private Recurso recurso;


    /* =========================
       GETTERS Y SETTERS
       ========================= */

    public Long getId() {
        return id;
    }

    // Opcional pero recomendado por consistencia
    public void setId(Long id) {
        this.id = id;
    }

    public int getCantidadAsignada() {
        return cantidadAsignada;
    }

    public void setCantidadAsignada(int cantidadAsignada) {
        this.cantidadAsignada = cantidadAsignada;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public Recurso getRecurso() {
        return recurso;
    }

    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }
}