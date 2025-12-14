package com.example.gestiontareas.service.imp;

import com.example.gestiontareas.dto.Request.ProyectoRequest;
import com.example.gestiontareas.dto.Response.ProyectoResponse;
import com.example.gestiontareas.Mapper.ProyectoMapper;
import com.example.gestiontareas.model.Proyecto;
import com.example.gestiontareas.repository.ProyectoRepository;
import com.example.gestiontareas.services.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProyectoServiceImp implements ProyectoService {

    private final ProyectoRepository proyectoRepository = null;
    private final ProyectoMapper proyectoMapper = new ProyectoMapper();

    @Override
    @Transactional
    public ProyectoResponse crearProyecto(ProyectoRequest request) {
        Proyecto proyecto = proyectoMapper.toEntity(request);
        Proyecto proyectoGuardado = proyectoRepository.save(proyecto);
        return proyectoMapper.toResponse(proyectoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ProyectoResponse obtenerProyectoPorId(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));
        return proyectoMapper.toResponse(proyecto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponse> obtenerTodosLosProyectos() {
        List<Proyecto> proyectos = proyectoRepository.findAll();
        return proyectoMapper.toResponseList(proyectos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponse> obtenerProyectosPorEstado(String estado) {
        List<Proyecto> proyectos = proyectoRepository.findByEstado(estado.toUpperCase());
        return proyectoMapper.toResponseList(proyectos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponse> buscarProyectosPorNombre(String nombre) {
        List<Proyecto> proyectos = proyectoRepository.findByNombreContainingIgnoreCase(nombre);
        return proyectoMapper.toResponseList(proyectos);
    }

    @Override
    @Transactional
    public ProyectoResponse actualizarProyecto(Long id, ProyectoRequest request) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));
        
        proyectoMapper.updateEntityFromRequest(proyecto, request);
        Proyecto proyectoActualizado = proyectoRepository.save(proyecto);
        return proyectoMapper.toResponse(proyectoActualizado);
    }

    @Override
    @Transactional
    public ProyectoResponse actualizarContadoresTareas(Long id, Integer pendientes, Integer enProceso, Integer terminadas) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));
        
        if (pendientes != null) {
            proyecto.setTareasPendientes(pendientes);
        }
        if (enProceso != null) {
            proyecto.setTareasEnProceso(enProceso);
        }
        if (terminadas != null) {
            proyecto.setTareasTerminadas(terminadas);
        }
        
        Proyecto proyectoActualizado = proyectoRepository.save(proyecto);
        return proyectoMapper.toResponse(proyectoActualizado);
    }

    @Override
    @Transactional
    public void eliminarProyecto(Long id) {
        if (!proyectoRepository.existsById(id)) {
            throw new RuntimeException("Proyecto no encontrado con ID: " + id);
        }
        proyectoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProyectoResponse cambiarEstadoProyecto(Long id, String nuevoEstado) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));
        
        proyecto.setEstado(nuevoEstado.toUpperCase());
        Proyecto proyectoActualizado = proyectoRepository.save(proyecto);
        return proyectoMapper.toResponse(proyectoActualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticasGenerales() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        // Total de proyectos
        Long totalProyectos = proyectoRepository.count();
        estadisticas.put("totalProyectos", totalProyectos);
        
        // Proyectos por estado
        Long proyectosActivos = proyectoRepository.countByEstado("ACTIVO");
        estadisticas.put("proyectosActivos", proyectosActivos);
        
        Long proyectosCompletados = proyectoRepository.countByEstado("COMPLETADO");
        estadisticas.put("proyectosCompletados", proyectosCompletados);
        
        Long proyectosPausados = proyectoRepository.countByEstado("PAUSADO");
        estadisticas.put("proyectosPausados", proyectosPausados);
        
        // Suma de tareas
        Long totalTareasEnProceso = proyectoRepository.sumTareasEnProceso();
        estadisticas.put("totalTareasEnProceso", totalTareasEnProceso != null ? totalTareasEnProceso : 0);
        
        Long totalTareasTerminadas = proyectoRepository.sumTareasTerminadas();
        estadisticas.put("totalTareasTerminadas", totalTareasTerminadas != null ? totalTareasTerminadas : 0);
        
        Long totalTareasPendientes = proyectoRepository.sumTareasPendientes();
        estadisticas.put("totalTareasPendientes", totalTareasPendientes != null ? totalTareasPendientes : 0);
        
        return estadisticas;
    }
}
