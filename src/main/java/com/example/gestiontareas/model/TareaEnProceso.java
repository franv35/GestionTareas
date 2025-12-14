package com.example.gestiontareas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tareas_en_proceso")
public class TareaEnProceso extends Tarea {

    public TareaEnProceso() {
        setEstado(Estado.ENPROCESO);
    }
}

