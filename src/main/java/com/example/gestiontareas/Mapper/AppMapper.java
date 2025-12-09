package com.example.gestiontareas.Mapper;

import java.util.stream.Collectors;

import com.example.gestiontareas.dto.Response.RecursoResponse;
import com.example.gestiontareas.dto.Response.TareaResponse;
import com.example.gestiontareas.dto.Response.UsuarioResponse;
import com.example.gestiontareas.model.Recurso;
import com.example.gestiontareas.model.TareaCreada;
import com.example.gestiontareas.model.TareaEnProceso;
import com.example.gestiontareas.model.TareaTerminada;
import com.example.gestiontareas.model.Usuario;

public class AppMapper {

    public static UsuarioResponse toUsuarioResponse(Usuario u) {
        if (u == null) return null;
        UsuarioResponse r = new UsuarioResponse();
        r.setId(u.getId());
        r.setNombre(u.getNombre());
        r.setEmail(u.getEmail());
        return r;
    }

    public static RecursoResponse toRecursoResponse(Recurso recurso) {
        if (recurso == null) return null;
        RecursoResponse r = new RecursoResponse();
        r.setId(recurso.getId());
        r.setNombre(recurso.getNombre());
        r.setCantidad(recurso.getCantidad());
        r.setUnidadMedida(recurso.getUnidadMedida());
        return r;
    }

    public static TareaResponse toTareaResponse(TareaCreada tarea) {
        if (tarea == null) return null;
        TareaResponse r = new TareaResponse();
        r.setId(tarea.getId());
        r.setTitulo(tarea.getTitulo());
        r.setDescripcion(tarea.getDescripcion());
        r.setFecha(tarea.getFecha());
        if (tarea.getUsuario() != null) r.setUsuarioId(tarea.getUsuario().getId());
        if (tarea.getRecursos() != null) {
            r.setRecursos(tarea.getRecursos()
                .stream()
                .map(AppMapper::toRecursoResponse)
                .collect(Collectors.toList()));
        }
        return r;
    }

    public static TareaResponse toTareaResponse(TareaEnProceso tarea) {
        if (tarea == null) return null;
        TareaResponse r = new TareaResponse();
        r.setId(tarea.getId());
        r.setTitulo(tarea.getTitulo());
        r.setDescripcion(tarea.getDescripcion());
        r.setFecha(tarea.getFecha());
        if (tarea.getUsuario() != null) r.setUsuarioId(tarea.getUsuario().getId());
        if (tarea.getRecursos() != null) {
            r.setRecursos(tarea.getRecursos()
                .stream()
                .map(AppMapper::toRecursoResponse)
                .collect(Collectors.toList()));
        }
        return r;
    }

    public static TareaResponse toTareaResponse(TareaTerminada tarea) {
        if (tarea == null) return null;
        TareaResponse r = new TareaResponse();
        r.setId(tarea.getId());
        r.setTitulo(tarea.getTitulo());
        r.setDescripcion(tarea.getDescripcion());
        r.setFecha(tarea.getFecha());
        if (tarea.getUsuario() != null) r.setUsuarioId(tarea.getUsuario().getId());
        if (tarea.getRecursos() != null) {
            r.setRecursos(tarea.getRecursos()
                .stream()
                .map(AppMapper::toRecursoResponse)
                .collect(Collectors.toList()));
        }
        return r;
    }
}
