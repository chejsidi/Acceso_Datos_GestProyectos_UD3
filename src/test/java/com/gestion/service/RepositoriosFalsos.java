package com.gestion.service;

import com.gestion.model.Gestion;
import com.gestion.model.Pieza;
import com.gestion.model.Proveedor;
import com.gestion.model.Proyecto;
import com.gestion.repository.GestionRepository;
import com.gestion.repository.PiezaRepository;
import com.gestion.repository.ProveedorRepository;
import com.gestion.repository.ProyectoRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Dobles de prueba: sustituyen a los repositorios reales guardando los datos en memoria
class RepositoriosFalsos {

    static class ProveedorRepoFalso extends ProveedorRepository {
        final Map<String, Proveedor> datos = new HashMap<>();
        long gestionesAsociadas = 0;

        @Override public void guardar(Proveedor p) { datos.put(p.getCodigo(), p); }
        @Override public void actualizar(Proveedor p) { datos.put(p.getCodigo(), p); }
        @Override public void eliminarPorId(String codigo) { datos.remove(codigo); }
        @Override public Optional<Proveedor> buscarPorId(String codigo) { return Optional.ofNullable(datos.get(codigo)); }
        @Override public List<Proveedor> listarTodos() { return new ArrayList<>(datos.values()); }
        @Override public long contarGestiones(String codigo) { return gestionesAsociadas; }
    }

    static class PiezaRepoFalso extends PiezaRepository {
        final Map<String, Pieza> datos = new HashMap<>();

        @Override public void guardar(Pieza p) { datos.put(p.getCodigo(), p); }
        @Override public Optional<Pieza> buscarPorId(String codigo) { return Optional.ofNullable(datos.get(codigo)); }
    }

    static class ProyectoRepoFalso extends ProyectoRepository {
        final Map<String, Proyecto> datos = new HashMap<>();

        @Override public void guardar(Proyecto p) { datos.put(p.getCodigo(), p); }
        @Override public Optional<Proyecto> buscarPorId(String codigo) { return Optional.ofNullable(datos.get(codigo)); }
    }

    static class GestionRepoFalso extends GestionRepository {
        final List<Gestion> datos = new ArrayList<>();

        @Override public void guardar(Gestion g) { datos.add(g); }
    }
}