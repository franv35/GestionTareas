package com.example.gestiontareas.Mapper;

import java.util.stream.Collectors;

import com.example.gestiontareas.dto.Response.*;
import com.example.gestiontareas.model.*;

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

        RecursoResponse res = new RecursoResponse();
        res.setId(recurso.getId());
        res.setNombre(recurso.getNombre());
        res.setCantidad(recurso.getStockDisponible());
        res.setUnidad(recurso.getUnidad());

        return res;
    }

    /* =========================
       TAREA_RECURSO
       ========================= */
    public static TareaRecursoResponse toTareaRecursoResponse(TareaRecurso tr) {
        if (tr == null) return null;

        TareaRecursoResponse r = new TareaRecursoResponse();
        r.setRecursoId(tr.getRecurso().getId());
        r.setNombreRecurso(tr.getRecurso().getNombre());
        r.setCantidadAsignada(tr.getCantidadAsignada());
        r.setUnidad(tr.getRecurso().getUnidad());

        return r;
    }

    /* =========================
       TAREA
       ========================= */
    public static TareaResponse toTareaResponse(Tarea tarea) {
        if (tarea == null) return null;

        TareaResponse r = new TareaResponse();
        r.setId(tarea.getId());
        r.setTitulo(tarea.getTitulo());
        r.setDescripcion(tarea.getDescripcion());
        r.setEstado(tarea.getEstado());
        r.setProyectoId(tarea.getProyecto().getId());

        if (tarea.getAsignaciones() != null) {
            r.setAsignaciones(
                tarea.getAsignaciones()
                     .stream()
                     .map(AppMapper::toTareaRecursoResponse)
                     .collect(Collectors.toList())
            );
        }

        return r;
    }
}