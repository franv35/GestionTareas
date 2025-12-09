package com.example.gestiontareas.service.imp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.gestiontareas.Mapper.AppMapper;
import com.example.gestiontareas.dto.Request.TareaRequest;
import com.example.gestiontareas.dto.Response.TareaResponse;
import com.example.gestiontareas.model.Recurso;
import com.example.gestiontareas.model.TareaCreada;
import com.example.gestiontareas.model.Usuario;
import com.example.gestiontareas.repository.RecursoRepository;
import com.example.gestiontareas.repository.TareaCreadaRepository;
import com.example.gestiontareas.repository.UsuarioRepository;
import com.example.gestiontareas.services.TareaCreadaService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TareaCreadaServiceImp implements TareaCreadaService {

    private final TareaCreadaRepository tareaCreadaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecursoRepository recursoRepository;

    public TareaCreadaServiceImp(TareaCreadaRepository tareaCreadaRepository,
                                 UsuarioRepository usuarioRepository,
                                 RecursoRepository recursoRepository) {
        this.tareaCreadaRepository = tareaCreadaRepository;
        this.usuarioRepository = usuarioRepository;
        this.recursoRepository = recursoRepository;
    }

    @Override
    public TareaResponse create(TareaRequest req) {
        Usuario u = usuarioRepository.findById(req.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<Recurso> recursos = null;
        if (req.getRecursoIds() != null && !req.getRecursoIds().isEmpty()) {
            recursos = req.getRecursoIds().stream()
                    .map(id -> recursoRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado con id: " + id)))
                    .collect(Collectors.toList());
        }

        TareaCreada tarea = new TareaCreada();
        tarea.setTitulo(req.getTitulo());
        tarea.setDescripcion(req.getDescripcion());
        tarea.setFecha(req.getFecha().toString());
        tarea.setUsuario(u);
        tarea.setRecursos(recursos);

        TareaCreada saved = tareaCreadaRepository.save(tarea);
        return AppMapper.toTareaResponse(saved);
    }

    @Override
    public List<TareaResponse> listByUsuario(Long usuarioId) {
        Usuario u = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return tareaCreadaRepository.findByUsuarioOrderByFechaDesc(u).stream()
                .map(AppMapper::toTareaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TareaResponse getById(Long id) {
        return tareaCreadaRepository.findById(id)
                .map(AppMapper::toTareaResponse)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
    }

    @Override
    public void delete(Long id) {
        if (!tareaCreadaRepository.existsById(id)) {
            throw new IllegalArgumentException("Tarea no encontrada");
        }
        tareaCreadaRepository.deleteById(id);
    }
}
