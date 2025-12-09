package com.example.gestiontareas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tareas_en_proceso")
public class TareaEnProceso extends Tarea {
    // Hereda: id, titulo, descripcion, fecha, usuario, recursos
    // Aquí podrías agregar atributos específicos de las tareas en proceso si lo necesitás
}
