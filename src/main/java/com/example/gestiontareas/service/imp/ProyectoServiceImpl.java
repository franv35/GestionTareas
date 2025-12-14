package com.example.gestiontareas.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gestiontareas.model.Proyecto;
import com.example.gestiontareas.model.Tarea;
import com.example.gestiontareas.model.Usuario;
import com.example.gestiontareas.repository.ProyectoRepository;
import com.example.gestiontareas.repository.TareaRepository;
import com.example.gestiontareas.repository.UsuarioRepository;
import com.example.gestiontareas.services.ProyectoService;

@Service
public class ProyectoServiceImpl implements ProyectoService {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TareaRepository tareaRepository;

    @Override
    public Proyecto crearProyecto(Long usuarioId, Proyecto proyecto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        proyecto.setUsuario(usuario);
        return proyectoRepository.save(proyecto);
    }

    @Override
    public Proyecto obtenerProyecto(Long id) {
        return proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
    }

    @Override
    public List<Proyecto> listarTodos() {
        return proyectoRepository.findAll();
    }

    @Override
    public Proyecto editarProyecto(Long id, Proyecto proyecto) {
        Proyecto existente = obtenerProyecto(id);

        existente.setNombre(proyecto.getNombre());
        existente.setDescripcion(proyecto.getDescripcion());

        return proyectoRepository.save(existente);
    }

    @Override
    public void eliminarProyecto(Long id) {
        Proyecto proyecto = obtenerProyecto(id);
        proyectoRepository.delete(proyecto);
    }

    @Override
    public List<Tarea> obtenerTareas(Long proyectoId) {
        Proyecto proyecto = obtenerProyecto(proyectoId);
        return proyecto.getTareas();
        // Alternativa:
        // return tareaRepository.findByProyectoId(proyectoId);
    }
}
