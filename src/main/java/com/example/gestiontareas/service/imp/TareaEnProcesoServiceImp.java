package com.example.gestiontareas.service.imp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gestiontareas.Mapper.AppMapper;
import com.example.gestiontareas.dto.Request.TareaRequest;
import com.example.gestiontareas.dto.Response.TareaResponse;
import com.example.gestiontareas.model.Estado;
import com.example.gestiontareas.model.Recurso;
import com.example.gestiontareas.model.TareaEnProceso;
import com.example.gestiontareas.model.TareaTerminada;
import com.example.gestiontareas.model.Usuario;
import com.example.gestiontareas.repository.RecursoRepository;
import com.example.gestiontareas.repository.TareaEnProcesoRepository;
import com.example.gestiontareas.repository.TareaTerminadaRepository;
import com.example.gestiontareas.repository.UsuarioRepository;
import com.example.gestiontareas.services.TareaEnProcesoService;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



@Service
public class TareaEnProcesoServiceImp implements TareaEnProcesoService {

    @Autowired
    private TareaEnProcesoRepository tareaEnProcesoRepository;

    @Autowired
    private TareaTerminadaRepository tareaTerminadaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    
    private String getCurrentDate() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }


    // =======================
    // MÉTODO MAPPER
    // =======================
    private TareaResponse mapToResponse(
            Long id,
            String titulo,
            String descripcion,
            String fecha,
            Long usuarioId,
            Estado estado
    ) {
        TareaResponse response = new TareaResponse();
        response.setId(id);
        response.setTitulo(titulo);
        response.setDescripcion(descripcion);
        response.setFecha(fecha);
        response.setUsuarioId(usuarioId);
        response.setEstado(estado.name());
        response.setRecursos(null); // o List.of()
        return response;
    }

    // =======================
    // MOVER A TERMINADA
    // =======================
    @Override
    public TareaResponse moverATerminada(Long id) {

        TareaEnProceso tareaEnProceso = tareaEnProcesoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        TareaTerminada tareaTerminada = new TareaTerminada();
        tareaTerminada.setTitulo(tareaEnProceso.getTitulo());
        tareaTerminada.setDescripcion(tareaEnProceso.getDescripcion());
        tareaTerminada.setEstado(Estado.TERMINADA);
        tareaTerminada.setUsuario(tareaEnProceso.getUsuario());
        tareaTerminada.setFecha(getCurrentDate());


        TareaTerminada tareaNueva = tareaTerminadaRepository.save(tareaTerminada);
        tareaEnProcesoRepository.deleteById(id);

        return mapToResponse(
                tareaNueva.getId(),
                tareaNueva.getTitulo(),
                tareaNueva.getDescripcion(),
                tareaNueva.getFecha(),
                tareaNueva.getUsuario().getId(),
                tareaNueva.getEstado()
        );
    }

    // =======================
    // MOVER DESDE TERMINADA
    // =======================
    @Override
    public TareaResponse moverDesdeTerminada(Long id) {

        TareaTerminada tareaTerminada = tareaTerminadaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        TareaEnProceso tareaEnProceso = new TareaEnProceso();
        tareaEnProceso.setTitulo(tareaTerminada.getTitulo());
        tareaEnProceso.setDescripcion(tareaTerminada.getDescripcion());
        tareaEnProceso.setEstado(Estado.ENPROCESO);
        tareaEnProceso.setUsuario(tareaTerminada.getUsuario());
        tareaEnProceso.setFecha(getCurrentDate());

        TareaEnProceso tareaNueva = tareaEnProcesoRepository.save(tareaEnProceso);
        tareaTerminadaRepository.deleteById(id);

        return mapToResponse(
                tareaNueva.getId(),
                tareaNueva.getTitulo(),
                tareaNueva.getDescripcion(),
                tareaNueva.getFecha(),
                tareaNueva.getUsuario().getId(),
                tareaNueva.getEstado()
        );
    }

    // =======================
    // CREATE
    // =======================
    @Override
    public TareaResponse create(TareaRequest request) {

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TareaEnProceso tarea = new TareaEnProceso();
        tarea.setTitulo(request.getTitulo());
        tarea.setDescripcion(request.getDescripcion());
        tarea.setEstado(Estado.ENPROCESO);
        tarea.setUsuario(usuario);
        tarea.setFecha(getCurrentDate());

        TareaEnProceso tareaNueva = tareaEnProcesoRepository.save(tarea);

        return mapToResponse(
                tareaNueva.getId(),
                tareaNueva.getTitulo(),
                tareaNueva.getDescripcion(),
                tareaNueva.getFecha(),
                tareaNueva.getUsuario().getId(),
                tareaNueva.getEstado()
        );
    }

    // =======================
    // GET BY ID
    // =======================
    @Override
    public TareaResponse getById(Long id) {

        TareaEnProceso tarea = tareaEnProcesoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        return mapToResponse(
                tarea.getId(),
                tarea.getTitulo(),
                tarea.getDescripcion(),
                tarea.getFecha(),
                tarea.getUsuario().getId(),
                tarea.getEstado()
        );
    }

    // =======================
    // DELETE
    // =======================
    @Override
    public void delete(Long id) {
        tareaEnProcesoRepository.deleteById(id);
    }


	@Override
	public List<TareaResponse> listByUsuario(Long usuarioId) {
		// TODO Auto-generated method stub
		return null;
	}
}
