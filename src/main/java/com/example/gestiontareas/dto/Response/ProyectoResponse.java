package com.example.gestiontareas.dto.Response;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class ProyectoResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer tareasPendientes;
    private Integer tareasEnProceso;
    private Integer tareasTerminadas;
    private Integer totalTareas;
    private Double porcentajeProgreso;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
