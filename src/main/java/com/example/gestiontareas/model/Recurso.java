package com.example.gestiontareas.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "recurso")
public class Recurso {

    /* =========================
       ID
       ========================= */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* =========================
       ATRIBUTOS
       ========================= */
    @Column(nullable = false)
    private String nombre;

    // Stock total cargado al proyecto
    @Column(nullable = false)
    private int stockTotal;

    // Stock disponible para asignar
    @Column(nullable = false)
    private int stockDisponible;

    @Column(nullable = false)
    private String unidad;

    /* =========================
       RELACIÓN CON PROYECTO
       Muchos recursos → Un proyecto
       ========================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    @JsonIgnore
    private Proyecto proyecto;

    /* =========================
       RELACIÓN CON TareaRecurso
       Un recurso → Muchas asignaciones
       ========================= */
    @OneToMany(
        mappedBy = "recurso",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<TareaRecurso> tareasAsignadas = new ArrayList<>();


    /* =========================
       GETTERS Y SETTERS
       ========================= */

    public Long getId() {
        return id;
    }

    // Opcional pero recomendado
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(int stockTotal) {
        this.stockTotal = stockTotal;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(int stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public List<TareaRecurso> getTareasAsignadas() {
        return tareasAsignadas;
    }

    public void setTareasAsignadas(List<TareaRecurso> tareasAsignadas) {
        this.tareasAsignadas = tareasAsignadas;
    }

    /* =========================
       MÉTODOS DE APOYO
       ========================= */

    public void agregarAsignacion(TareaRecurso asignacion) {
        tareasAsignadas.add(asignacion);
        asignacion.setRecurso(this);
    }

    public void removerAsignacion(TareaRecurso asignacion) {
        tareasAsignadas.remove(asignacion);
        asignacion.setRecurso(null);
    }
}