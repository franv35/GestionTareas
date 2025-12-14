package com.example.gestiontareas.dto.Request;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProyectoRequest {

    @NotBlank(message = "El nombre del proyecto es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private Integer tareasPendientes;

    private Integer tareasEnProceso;

    private Integer tareasTerminadas;

    private String estado;

	
}

