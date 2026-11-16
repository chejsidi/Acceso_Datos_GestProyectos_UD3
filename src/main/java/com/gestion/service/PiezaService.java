package com.gestion.service;

import com.gestion.exception.EntidadNoEncontradaException;
import com.gestion.exception.IntegridadException;
import com.gestion.exception.ValidacionException;
import com.gestion.model.Pieza;
import com.gestion.repository.PiezaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class PiezaService {

    private final PiezaRepository repository;
    private static final Logger log = LoggerFactory.getLogger(PiezaService.class);

    public PiezaService(PiezaRepository repository) {
        this.repository = repository;
    }

    public void alta(Pieza pieza) {
        validar(pieza);
        if (repository.existe(pieza.getCodigo())) {
            throw new ValidacionException("Ya existe una pieza con el código " + pieza.getCodigo() + ".");
        }
        repository.guardar(pieza);
        log.info("Alta de pieza {}", pieza.getCodigo());
    }

    public void baja(String codigo) {
        buscarPorCodigo(codigo);
        long suministros = repository.contarGestiones(codigo);
        if (suministros > 0) {
            throw new IntegridadException("No se puede eliminar: la pieza aparece en "
                    + suministros + " suministro(s).");
        }
        repository.eliminarPorId(codigo);
        log.info("Baja de pieza {}", codigo);
    }

    public void modificar(Pieza pieza) {
        validar(pieza);
        buscarPorCodigo(pieza.getCodigo());
        repository.actualizar(pieza);
        log.info("Modificación de pieza {}", pieza.getCodigo());
    }

    public Pieza buscarPorCodigo(String codigo) {
        return repository.buscarPorId(codigo)
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe ninguna pieza con código " + codigo + "."));
    }

    public boolean existe(String codigo) {
        return repository.existe(codigo);
    }

    public List<Pieza> buscarPorNombre(String nombre) {
        return repository.buscarPorNombre(nombre);
    }

    public List<Pieza> listarTodas() {
        return repository.listarTodos();
    }

    private void validar(Pieza p) {
        if (p == null) throw new ValidacionException("La pieza no puede ser nula.");
        if (p.getCodigo() == null || p.getCodigo().isBlank()) throw new ValidacionException("El código es obligatorio.");
        if (p.getNombre() == null || p.getNombre().isBlank()) throw new ValidacionException("El nombre es obligatorio.");
        if (p.getPrecio() == null || p.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidacionException("El precio es obligatorio y no puede ser negativo.");
        }
    }

    public List<Pieza> busquedaAvanzada(String nombre, BigDecimal precioMinimo, BigDecimal precioMaximo,
                                        String codigoProveedor, String codigoProyecto) {
        if (precioMinimo != null && precioMaximo != null && precioMinimo.compareTo(precioMaximo) > 0) {
            throw new ValidacionException("El precio mínimo no puede ser mayor que el máximo.");
        }
        return repository.busquedaAvanzada(nombre, precioMinimo, precioMaximo, codigoProveedor, codigoProyecto);
    }

    public long contar() {
        return repository.contar();
    }
}