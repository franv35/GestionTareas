package com.example.gestiontareas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tareas_terminadas")
public class TareaTerminada extends Tarea {
    // Hereda: id, titulo, descripcion, fecha, usuario, recursos
    // Aquí podrías agregar atributos específicos de las tareas terminadas si lo necesitás
}
