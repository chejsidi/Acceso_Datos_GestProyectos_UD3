package com.gestion.service;

import com.gestion.exception.EntidadNoEncontradaException;
import com.gestion.exception.ValidacionException;
import com.gestion.model.Gestion;
import com.gestion.model.Pieza;
import com.gestion.model.Proveedor;
import com.gestion.model.Proyecto;
import com.gestion.repository.GestionRepository;

import java.time.LocalDate;
import java.util.List;

public class GestionService {

    private final GestionRepository repository;
    private final ProveedorService proveedorService;
    private final PiezaService piezaService;
    private final ProyectoService proyectoService;

    // Un servicio puede depender de otros servicios (como en Angular)
    public GestionService(GestionRepository repository, ProveedorService proveedorService,
                          PiezaService piezaService, ProyectoService proyectoService) {
        this.repository = repository;
        this.proveedorService = proveedorService;
        this.piezaService = piezaService;
        this.proyectoService = proyectoService;
    }

    public void alta(Gestion gestion) {
        validar(gestion);
        repository.guardar(gestion);
    }

    public void baja(Long id) {
        buscarPorId(id);
        repository.eliminarPorId(id);
    }

    public void modificar(Gestion gestion) {
        validar(gestion);
        buscarPorId(gestion.getId());
        repository.actualizar(gestion);
    }

    public Gestion buscarPorId(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe ningún suministro con id " + id + "."));
    }

    public List<Gestion> listarTodas() {
        return repository.listarTodos();
    }

    // Antes de buscar comprobamos que el proveedor existe, para dar un mensaje claro
    public List<Gestion> buscarPorProveedor(String codigo) {
        proveedorService.buscarPorCodigo(codigo);
        return repository.buscarPorProveedor(codigo);
    }

    public List<Gestion> buscarPorPieza(String codigo) {
        piezaService.buscarPorCodigo(codigo);
        return repository.buscarPorPieza(codigo);
    }

    public List<Gestion> buscarPorProyecto(String codigo) {
        proyectoService.buscarPorCodigo(codigo);
        return repository.buscarPorProyecto(codigo);
    }

    // El menú los usa para validar cada código nada más escribirlo
    public Proveedor obtenerProveedor(String codigo) {
        return proveedorService.buscarPorCodigo(codigo);
    }

    public Pieza obtenerPieza(String codigo) {
        return piezaService.buscarPorCodigo(codigo);
    }

    public Proyecto obtenerProyecto(String codigo) {
        return proyectoService.buscarPorCodigo(codigo);
    }

    private void validar(Gestion g) {
        if (g == null) throw new ValidacionException("El suministro no puede ser nulo.");
        if (g.getProveedor() == null || g.getPieza() == null || g.getProyecto() == null) {
            throw new ValidacionException("El suministro debe tener proveedor, pieza y proyecto.");
        }
        if (g.getFecha() == null) throw new ValidacionException("La fecha es obligatoria.");
        if (g.getFecha().isAfter(LocalDate.now())) throw new ValidacionException("La fecha no puede ser futura.");
        if (g.getCantidad() <= 0) throw new ValidacionException("La cantidad debe ser mayor que 0.");

        // Las entidades relacionadas deben existir en la BD
        proveedorService.buscarPorCodigo(g.getProveedor().getCodigo());
        piezaService.buscarPorCodigo(g.getPieza().getCodigo());
        proyectoService.buscarPorCodigo(g.getProyecto().getCodigo());
    }
}