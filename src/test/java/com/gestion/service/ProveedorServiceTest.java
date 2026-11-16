package com.gestion.service;

import com.gestion.exception.EntidadNoEncontradaException;
import com.gestion.exception.IntegridadException;
import com.gestion.exception.ValidacionException;
import com.gestion.model.Proveedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProveedorServiceTest {

    private RepositoriosFalsos.ProveedorRepoFalso repo;
    private ProveedorService service;

    // Se ejecuta antes de cada test: cada test empieza con datos limpios
    @BeforeEach
    void preparar() {
        repo = new RepositoriosFalsos.ProveedorRepoFalso();
        service = new ProveedorService(repo);
    }

    @Test
    @DisplayName("Alta correcta de un proveedor")
    void altaCorrecta() {
        service.alta(new Proveedor("P01", "Ana", "García", "Calle Mayor 1"));
        assertTrue(repo.datos.containsKey("P01"));
    }

    @Test
    @DisplayName("No se permite dar de alta un código repetido")
    void altaCodigoDuplicado() {
        service.alta(new Proveedor("P01", "Ana", "García", "Calle Mayor 1"));
        assertThrows(ValidacionException.class,
                () -> service.alta(new Proveedor("P01", "Luis", "Pérez", "Calle Sol 2")));
    }

    @Test
    @DisplayName("No se permite un proveedor sin nombre")
    void altaSinNombre() {
        assertThrows(ValidacionException.class,
                () -> service.alta(new Proveedor("P02", "", "García", "Calle Mayor 1")));
    }

    @Test
    @DisplayName("No se puede borrar un proveedor con suministros (integridad referencial)")
    void bajaConSuministros() {
        service.alta(new Proveedor("P01", "Ana", "García", "Calle Mayor 1"));
        repo.gestionesAsociadas = 3;
        assertThrows(IntegridadException.class, () -> service.baja("P01"));
        assertTrue(repo.datos.containsKey("P01"), "El proveedor no debe haberse borrado");
    }

    @Test
    @DisplayName("Se puede borrar un proveedor sin suministros")
    void bajaCorrecta() {
        service.alta(new Proveedor("P01", "Ana", "García", "Calle Mayor 1"));
        service.baja("P01");
        assertFalse(repo.datos.containsKey("P01"));
    }

    @Test
    @DisplayName("Buscar un código inexistente lanza excepción")
    void buscarInexistente() {
        assertThrows(EntidadNoEncontradaException.class, () -> service.buscarPorCodigo("NOEXISTE"));
    }
}