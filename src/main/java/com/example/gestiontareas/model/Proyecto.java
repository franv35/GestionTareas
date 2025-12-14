package com.example.gestiontareas.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "proyectos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "tareas_pendientes")
    private Integer tareasPendientes = 0;

    @Column(name = "tareas_en_proceso")
    private Integer tareasEnProceso = 0;

    @Column(name = "tareas_terminadas")
    private Integer tareasTerminadas = 0;

    @Column(name = "estado")
    private String estado = "ACTIVO"; // ACTIVO, PAUSADO, COMPLETADO, CANCELADO

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    // Método auxiliar para calcular el total de tareas
    public Integer getTotalTareas() {
        return (tareasPendientes != null ? tareasPendientes : 0) + 
               (tareasEnProceso != null ? tareasEnProceso : 0) + 
               (tareasTerminadas != null ? tareasTerminadas : 0);
    }

    // Método auxiliar para calcular el porcentaje de progreso
    public Double getPorcentajeProgreso() {
        Integer total = getTotalTareas();
        if (total == 0) {
            return 0.0;
        }
        return ((tareasTerminadas != null ? tareasTerminadas : 0) * 100.0) / total;
    }

	
}
