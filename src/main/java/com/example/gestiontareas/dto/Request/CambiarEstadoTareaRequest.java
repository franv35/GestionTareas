package com.example.gestiontareas.dto.Request;

import com.example.gestiontareas.model.EstadoTarea;

import jakarta.validation.constraints.NotNull;

public class CambiarEstadoTareaRequest {

    @NotNull
    private EstadoTarea estado;

    public EstadoTarea getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }
}
