package com.gestion.service;

import com.gestion.exception.EntidadNoEncontradaException;
import com.gestion.exception.IntegridadException;
import com.gestion.exception.ValidacionException;
import com.gestion.model.Proveedor;
import com.gestion.repository.ProveedorRepository;

import java.util.List;

// Reglas de negocio de proveedores. El menú nunca habla directamente con el repositorio
public class ProveedorService {

    private final ProveedorRepository repository;

    // El repositorio se recibe desde fuera (inyección de dependencias manual, como en Angular)
    public ProveedorService(ProveedorRepository repository) {
        this.repository = repository;
    }

    public void alta(Proveedor proveedor) {
        validar(proveedor);
        if (repository.existe(proveedor.getCodigo())) {
            throw new ValidacionException("Ya existe un proveedor con el código " + proveedor.getCodigo() + ".");
        }
        repository.guardar(proveedor);
    }

    public void baja(String codigo) {
        buscarPorCodigo(codigo);
        long suministros = repository.contarGestiones(codigo);
        if (suministros > 0) {
            throw new IntegridadException("No se puede eliminar: el proveedor tiene "
                    + suministros + " suministro(s) asociado(s).");
        }
        repository.eliminarPorId(codigo);
    }

    public void modificar(Proveedor proveedor) {
        validar(proveedor);
        buscarPorCodigo(proveedor.getCodigo());
        repository.actualizar(proveedor);
    }

    public Proveedor buscarPorCodigo(String codigo) {
        return repository.buscarPorId(codigo)
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe ningún proveedor con código " + codigo + "."));
    }

    public boolean existe(String codigo) {
        return repository.existe(codigo);
    }

    public List<Proveedor> buscarPorNombre(String nombre) {
        return repository.buscarPorNombre(nombre);
    }

    public List<Proveedor> buscarPorDireccion(String direccion) {
        return repository.buscarPorDireccion(direccion);
    }

    public List<Proveedor> listarTodos() {
        return repository.listarTodos();
    }

    // Segunda capa de validación: el servicio no confía en que quien lo llame haya validado
    private void validar(Proveedor p) {
        if (p == null) throw new ValidacionException("El proveedor no puede ser nulo.");
        if (estaVacio(p.getCodigo())) throw new ValidacionException("El código es obligatorio.");
        if (estaVacio(p.getNombre())) throw new ValidacionException("El nombre es obligatorio.");
        if (estaVacio(p.getApellidos())) throw new ValidacionException("Los apellidos son obligatorios.");
        if (estaVacio(p.getDireccion())) throw new ValidacionException("La dirección es obligatoria.");
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}