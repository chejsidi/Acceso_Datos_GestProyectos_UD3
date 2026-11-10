package com.gestion.service;

import com.gestion.exception.EntidadNoEncontradaException;
import com.gestion.exception.IntegridadException;
import com.gestion.exception.ValidacionException;
import com.gestion.model.Proyecto;
import com.gestion.repository.ProyectoRepository;

import java.util.List;

public class ProyectoService {

    private final ProyectoRepository repository;

    public ProyectoService(ProyectoRepository repository) {
        this.repository = repository;
    }

    public void alta(Proyecto proyecto) {
        validar(proyecto);
        if (repository.existe(proyecto.getCodigo())) {
            throw new ValidacionException("Ya existe un proyecto con el código " + proyecto.getCodigo() + ".");
        }
        repository.guardar(proyecto);
    }

    public void baja(String codigo) {
        buscarPorCodigo(codigo);
        long suministros = repository.contarGestiones(codigo);
        if (suministros > 0) {
            throw new IntegridadException("No se puede eliminar: el proyecto tiene "
                    + suministros + " suministro(s) asociado(s).");
        }
        repository.eliminarPorId(codigo);
    }

    public void modificar(Proyecto proyecto) {
        validar(proyecto);
        buscarPorCodigo(proyecto.getCodigo());
        repository.actualizar(proyecto);
    }

    public Proyecto buscarPorCodigo(String codigo) {
        return repository.buscarPorId(codigo)
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe ningún proyecto con código " + codigo + "."));
    }

    public boolean existe(String codigo) {
        return repository.existe(codigo);
    }

    public List<Proyecto> buscarPorNombre(String nombre) {
        return repository.buscarPorNombre(nombre);
    }

    public List<Proyecto> buscarPorCiudad(String ciudad) {
        return repository.buscarPorCiudad(ciudad);
    }

    public List<Proyecto> listarTodos() {
        return repository.listarTodos();
    }

    private void validar(Proyecto p) {
        if (p == null) throw new ValidacionException("El proyecto no puede ser nulo.");
        if (p.getCodigo() == null || p.getCodigo().isBlank()) throw new ValidacionException("El código es obligatorio.");
        if (p.getNombre() == null || p.getNombre().isBlank()) throw new ValidacionException("El nombre es obligatorio.");
        if (p.getCiudad() == null || p.getCiudad().isBlank()) throw new ValidacionException("La ciudad es obligatoria.");
    }
}