package com.example.gestiontareas.service.imp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gestiontareas.dto.Request.TareaRequest;
import com.example.gestiontareas.dto.Response.TareaResponse;
import com.example.gestiontareas.model.Estado;
import com.example.gestiontareas.model.TareaCreada;
import com.example.gestiontareas.model.TareaEnProceso;
import com.example.gestiontareas.model.Usuario;
import com.example.gestiontareas.repository.TareaCreadaRepository;
import com.example.gestiontareas.repository.TareaEnProcesoRepository;
import com.example.gestiontareas.repository.UsuarioRepository;
import com.example.gestiontareas.services.TareaCreadaService;

import jakarta.transaction.Transactional; // Importación necesaria para @Transactional

import java.util.Optional;

@Service
public class TareaCreadaServiceImp implements TareaCreadaService {

    @Autowired
    private TareaCreadaRepository tareaCreadaRepository;

    @Autowired
    private TareaEnProcesoRepository tareaEnProcesoRepository;

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
    // MOVER A EN PROCESO
    // =======================
    @Override
    @Transactional // Para asegurar la consistencia al mover entre tablas
    public TareaResponse moverAEnProceso(Long id) {
        TareaCreada tareaCreada = tareaCreadaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        TareaEnProceso tareaEnProceso = new TareaEnProceso();
        tareaEnProceso.setTitulo(tareaCreada.getTitulo());
        tareaEnProceso.setDescripcion(tareaCreada.getDescripcion());
        tareaEnProceso.setEstado(Estado.ENPROCESO);
        tareaEnProceso.setUsuario(tareaCreada.getUsuario());
        tareaEnProceso.setFecha(getCurrentDate());

        TareaEnProceso tareaNueva = tareaEnProcesoRepository.save(tareaEnProceso);
        tareaCreadaRepository.deleteById(id);

        return mapToResponse(
                tareaNueva.getId(),
                tareaNueva.getTitulo(),
                tareaNueva.getDescripcion(),
                tareaNueva.getFecha(),
                tareaNueva.getUsuario().getId(), // Asumo que getUsuario() devuelve un objeto con getId()
                tareaNueva.getEstado()
        );
    }

    // =======================
    // MOVER DESDE EN PROCESO
    // =======================
    @Override
    @Transactional // Para asegurar la consistencia al mover entre tablas
    public TareaResponse moverDesdeEnProceso(Long id) {
        TareaEnProceso tareaEnProceso = tareaEnProcesoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        TareaCreada tareaCreada = new TareaCreada();
        tareaCreada.setTitulo(tareaEnProceso.getTitulo());
        tareaCreada.setDescripcion(tareaEnProceso.getDescripcion());
        tareaCreada.setEstado(Estado.CREADA);
        tareaCreada.setUsuario(tareaEnProceso.getUsuario());
        tareaCreada.setFecha(getCurrentDate());

        TareaCreada tareaNueva = tareaCreadaRepository.save(tareaCreada);
        tareaEnProcesoRepository.deleteById(id);

        return mapToResponse(
                tareaNueva.getId(),
                tareaNueva.getTitulo(),
                tareaNueva.getDescripcion(),
                tareaNueva.getFecha(),
                tareaNueva.getUsuario().getId(), // Asumo que getUsuario() devuelve un objeto con getId()
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

        TareaCreada tarea = new TareaCreada();
        tarea.setTitulo(request.getTitulo());
        tarea.setDescripcion(request.getDescripcion());
        tarea.setEstado(Estado.CREADA);
        tarea.setUsuario(usuario);
        tarea.setFecha(getCurrentDate());

        TareaCreada tareaNueva = tareaCreadaRepository.save(tarea);

        return mapToResponse(
                tareaNueva.getId(),
                tareaNueva.getTitulo(),
                tareaNueva.getDescripcion(),
                tareaNueva.getFecha(),
                tareaNueva.getUsuario().getId(), // Asumo que getUsuario() devuelve un objeto con getId()
                tareaNueva.getEstado()
        );
    }

    // =======================
 // =======================
    // LIST BY USUARIO
    // =======================
    @Override
    public List<TareaResponse> listByUsuario(Long usuarioId) {
        // Corrección: Usar el repositorio de TareaCreada y el método findByUsuarioId
    	Optional<TareaCreada> tareas = tareaCreadaRepository.findById
    			(usuarioId);
        
        // Corrección: Ahora 'tareas' es una lista, se puede usar .stream() directamente
        return tareas.stream()
                .map(t -> mapToResponse(
                        t.getId(),
                        t.getTitulo(),
                        t.getDescripcion(),
                        t.getFecha(),
                        t.getUsuario().getId(),
                        t.getEstado()))
                .collect(Collectors.toList()); // Uso de Collectors.toList() para consistencia
    }

    // =======================
    // GET BY ID
    // =======================
    @Override
    public TareaResponse getById(Long id) {
        TareaCreada tarea = tareaCreadaRepository.findById(id)
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
        tareaCreadaRepository.deleteById(id);
    }
}