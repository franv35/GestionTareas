package com.example.gestiontareas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tareas_terminadas")
public class TareaTerminada extends Tarea {

    public TareaTerminada() {
        setEstado(Estado.TERMINADA);
    }
}

