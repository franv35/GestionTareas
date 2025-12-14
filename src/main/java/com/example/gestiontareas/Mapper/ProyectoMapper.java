package com.example.gestiontareas.Mapper;

import com.example.gestiontareas.dto.Request.ProyectoRequest;
import com.example.gestiontareas.dto.Response.ProyectoResponse;
import com.example.gestiontareas.model.Proyecto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProyectoMapper {

    /**
     * Convierte un ProyectoRequest a entidad Proyecto
     */
    public Proyecto toEntity(ProyectoRequest request) {
        if (request == null) {
            return null;
        }

        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(request.getNombre());
        proyecto.setDescripcion(request.getDescripcion());
        proyecto.setFechaInicio(request.getFechaInicio());
        proyecto.setFechaFin(request.getFechaFin());
        proyecto.setTareasPendientes(request.getTareasPendientes() != null ? request.getTareasPendientes() : 0);
        proyecto.setTareasEnProceso(request.getTareasEnProceso() != null ? request.getTareasEnProceso() : 0);
        proyecto.setTareasTerminadas(request.getTareasTerminadas() != null ? request.getTareasTerminadas() : 0);
        proyecto.setEstado(request.getEstado() != null ? request.getEstado() : "ACTIVO");

        return proyecto;
    }

    /**
     * Convierte una entidad Proyecto a ProyectoResponse
     */
    public ProyectoResponse toResponse(Proyecto proyecto) {
        if (proyecto == null) {
            return null;
        }

        return ProyectoResponse.builder()
                .id(proyecto.getId())
                .nombre(proyecto.getNombre())
                .descripcion(proyecto.getDescripcion())
                .fechaInicio(proyecto.getFechaInicio())
                .fechaFin(proyecto.getFechaFin())
                .tareasPendientes(proyecto.getTareasPendientes())
                .tareasEnProceso(proyecto.getTareasEnProceso())
                .tareasTerminadas(proyecto.getTareasTerminadas())
                .totalTareas(proyecto.getTotalTareas())
                .porcentajeProgreso(proyecto.getPorcentajeProgreso())
                .estado(proyecto.getEstado())
                .fechaCreacion(proyecto.getFechaCreacion())
                .fechaActualizacion(proyecto.getFechaActualizacion())
                .build();
    }

    /**
     * Convierte una lista de entidades Proyecto a lista de ProyectoResponse
     */
    public List<ProyectoResponse> toResponseList(List<Proyecto> proyectos) {
        if (proyectos == null) {
            return null;
        }

        return proyectos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza una entidad Proyecto existente con los datos de un ProyectoRequest
     */
    public void updateEntityFromRequest(Proyecto proyecto, ProyectoRequest request) {
        if (proyecto == null || request == null) {
            return;
        }

        if (request.getNombre() != null) {
            proyecto.setNombre(request.getNombre());
        }
        if (request.getDescripcion() != null) {
            proyecto.setDescripcion(request.getDescripcion());
        }
        if (request.getFechaInicio() != null) {
            proyecto.setFechaInicio(request.getFechaInicio());
        }
        if (request.getFechaFin() != null) {
            proyecto.setFechaFin(request.getFechaFin());
        }
        if (request.getTareasPendientes() != null) {
            proyecto.setTareasPendientes(request.getTareasPendientes());
        }
        if (request.getTareasEnProceso() != null) {
            proyecto.setTareasEnProceso(request.getTareasEnProceso());
        }
        if (request.getTareasTerminadas() != null) {
            proyecto.setTareasTerminadas(request.getTareasTerminadas());
        }
        if (request.getEstado() != null) {
            proyecto.setEstado(request.getEstado());
        }
    }
}

