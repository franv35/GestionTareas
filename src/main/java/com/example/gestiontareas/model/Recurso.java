package com.example.gestiontareas.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "recursos")
public class Recurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;


    // Nombre del recurso (ej: Harina, Silla, Persona)
    @Column(nullable = false)
    private String nombre;

    // Cantidad asignada (ej: 20, 5, 3)
    @Column(nullable = false)
    private Double cantidad;

    // Unidad de medida (ej: kg, unidades, personas)
    @Column(nullable = false)
    private String unidadMedida;

    // Relación inversa con tareas (ManyToMany)
    @ManyToMany(mappedBy = "recursos")
    private List<TareaCreada> tareasCreadas;

    @ManyToMany(mappedBy = "recursos")
    private List<TareaEnProceso> tareasEnProceso;

    @ManyToMany(mappedBy = "recursos")
    private List<TareaTerminada> tareasTerminadas;

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getCantidad() { return cantidad; }
    public void setCantidad(Double cantidad) { this.cantidad = cantidad; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public List<TareaCreada> getTareasCreadas() { return tareasCreadas; }
    public void setTareasCreadas(List<TareaCreada> tareasCreadas) { this.tareasCreadas = tareasCreadas; }

    public List<TareaEnProceso> getTareasEnProceso() { return tareasEnProceso; }
    public void setTareasEnProceso(List<TareaEnProceso> tareasEnProceso) { this.tareasEnProceso = tareasEnProceso; }

    public List<TareaTerminada> getTareasTerminadas() { return tareasTerminadas; }
    public void setTareasTerminadas(List<TareaTerminada> tareasTerminadas) { this.tareasTerminadas = tareasTerminadas; }
}
