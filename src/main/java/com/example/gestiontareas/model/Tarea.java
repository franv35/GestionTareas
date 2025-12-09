package com.example.gestiontareas.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // importante: IDENTITY no funciona bien con TABLE_PER_CLASS
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String descripcion;

    @Column(nullable = false)
    private String fecha; // ISO string (yyyy-MM-dd)

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
        name = "tarea_recursos",
        joinColumns = @JoinColumn(name = "tarea_id"),
        inverseJoinColumns = @JoinColumn(name = "recurso_id")
    )
    private List<Recurso> recursos;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<Recurso> getRecursos() { return recursos; }
    public void setRecursos(List<Recurso> recursos) { this.recursos = recursos; }
}
