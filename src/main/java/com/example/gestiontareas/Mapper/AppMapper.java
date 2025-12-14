package com.example.gestiontareas.Mapper;

import java.util.stream.Collectors;

import com.example.gestiontareas.dto.Response.ProyectoResponse;
import com.example.gestiontareas.dto.Response.RecursoResponse;
import com.example.gestiontareas.dto.Response.TareaResponse;
import com.example.gestiontareas.dto.Response.UsuarioResponse;
import com.example.gestiontareas.model.Proyecto;
import com.example.gestiontareas.model.Recurso;
import com.example.gestiontareas.model.Tarea;
import com.example.gestiontareas.model.Usuario;

public class AppMapper {

    /* =========================
       USUARIO
       ========================= */
    public static UsuarioResponse toUsuarioResponse(Usuario u) {
        if (u == null) return null;

        UsuarioResponse r = new UsuarioResponse();
        r.setId(u.getId());
        r.setNombre(u.getNombre());
        r.setEmail(u.getEmail());
        return r;
    }

    /* =========================
       PROYECTO
       ========================= */
    public static ProyectoResponse toProyectoResponse(Proyecto p) {
        if (p == null) return null;

        ProyectoResponse r = new ProyectoResponse();
        r.setId(p.getId());
        r.setNombre(p.getNombre());
        r.setUsuarioId(p.getUsuario().getId());

        if (p.getTareas() != null) {
            r.setTareas(
                p.getTareas()
                 .stream()
                 .map(AppMapper::toTareaResponse)
                 .collect(Collectors.toList())
            );
        }

        return r;
    }

    /* =========================
       RECURSO
       ========================= */
    public static RecursoResponse toRecursoResponse(Recurso recurso) {
        if (recurso == null) return null;

        RecursoResponse r = new RecursoResponse();
        r.setId(recurso.getId());
        r.setNombre(recurso.getNombre());
        r.setCantidad(recurso.getCantidad());
        r.setUnidad(recurso.getUnidad());
        return r;
    }

    /* =========================
       TAREA (ÚNICA)
       ========================= */
    public static TareaResponse toTareaResponse(Tarea tarea) {
        if (tarea == null) return null;

        TareaResponse r = new TareaResponse();
        r.setId(tarea.getId());
        r.setTitulo(tarea.getTitulo());
        r.setDescripcion(tarea.getDescripcion());
        r.setEstado(tarea.getEstado()); // ENUM EstadoTarea
        r.setProyectoId(tarea.getProyecto().getId());

        if (tarea.getRecursos() != null) {
            r.setRecursos(
                tarea.getRecursos()
                     .stream()
                     .map(AppMapper::toRecursoResponse)
                     .collect(Collectors.toList())
            );
        }

        return r;
    }
}
