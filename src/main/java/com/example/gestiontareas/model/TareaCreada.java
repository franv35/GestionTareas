package com.example.gestiontareas.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "tareas_creadas")
public class TareaCreada extends Tarea {

    public TareaCreada() {
        setEstado(Estado.CREADA);  // ESTADO CORRECTO
    }

	public Object getFechaActualizacion() {
		// TODO Auto-generated method stub
		return null;
	}
}
