package com.example.gestiontareas.services;

import java.util.List;

import com.example.gestiontareas.model.Proyecto;
import com.example.gestiontareas.model.Tarea;

public interface ProyectoService {

    Proyecto crearProyecto(Long usuarioId, Proyecto proyecto);

    Proyecto obtenerProyecto(Long id);

    List<Proyecto> listarPorUsuario(Long usuarioId); // 🔥 CAMBIO

    Proyecto editarProyecto(Long id, Proyecto proyecto);

    void eliminarProyecto(Long id);

    List<Tarea> obtenerTareas(Long proyectoId);
    
    Proyecto marcarComoTerminado(Long id);

}
