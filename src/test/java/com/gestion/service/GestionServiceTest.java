package com.gestion.service;

import com.gestion.exception.EntidadNoEncontradaException;
import com.gestion.exception.ValidacionException;
import com.gestion.model.Gestion;
import com.gestion.model.Pieza;
import com.gestion.model.Proveedor;
import com.gestion.model.Proyecto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GestionServiceTest {

    private RepositoriosFalsos.GestionRepoFalso gestionRepo;
    private GestionService service;
    private Proveedor proveedor;
    private Pieza pieza;
    private Proyecto proyecto;

    @BeforeEach
    void preparar() {
        var proveedorRepo = new RepositoriosFalsos.ProveedorRepoFalso();
        var piezaRepo = new RepositoriosFalsos.PiezaRepoFalso();
        var proyectoRepo = new RepositoriosFalsos.ProyectoRepoFalso();
        gestionRepo = new RepositoriosFalsos.GestionRepoFalso();

        proveedor = new Proveedor("P01", "Ana", "García", "Calle Mayor 1");
        pieza = new Pieza("PZ01", "Tornillo", new BigDecimal("0.50"), "Tornillo M8");
        proyecto = new Proyecto("PR01", "Puente", "Vitoria");
        proveedorRepo.guardar(proveedor);
        piezaRepo.guardar(pieza);
        proyectoRepo.guardar(proyecto);

        service = new GestionService(gestionRepo,
                new ProveedorService(proveedorRepo),
                new PiezaService(piezaRepo),
                new ProyectoService(proyectoRepo));
    }

    @Test
    @DisplayName("Alta correcta de un suministro")
    void altaCorrecta() {
        service.alta(new Gestion(LocalDate.now(), 10, proveedor, pieza, proyecto));
        assertEquals(1, gestionRepo.datos.size());
    }

    @Test
    @DisplayName("La cantidad debe ser mayor que 0")
    void cantidadCero() {
        assertThrows(ValidacionException.class,
                () -> service.alta(new Gestion(LocalDate.now(), 0, proveedor, pieza, proyecto)));
    }

    @Test
    @DisplayName("La fecha no puede ser futura")
    void fechaFutura() {
        assertThrows(ValidacionException.class,
                () -> service.alta(new Gestion(LocalDate.now().plusDays(1), 5, proveedor, pieza, proyecto)));
    }

    @Test
    @DisplayName("No se puede registrar un suministro de un proveedor inexistente")
    void proveedorInexistente() {
        Proveedor fantasma = new Proveedor("XX99", "No", "Existe", "-");
        assertThrows(EntidadNoEncontradaException.class,
                () -> service.alta(new Gestion(LocalDate.now(), 5, fantasma, pieza, proyecto)));
        assertTrue(gestionRepo.datos.isEmpty());
    }
}