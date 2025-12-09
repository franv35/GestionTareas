package com.example.gestiontareas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tareas_creadas")
public class TareaCreada extends Tarea {
    // No necesita volver a declarar atributos comunes
    // Hereda: id, titulo, descripcion, fecha, usuario, recursos
    // Si en el futuro querés agregar campos específicos de "creada", se ponen aquí
}
