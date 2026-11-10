package com.gestion.repository;

import com.gestion.model.Gestion;

import java.util.List;

public class GestionRepository extends RepositorioBase<Gestion, Long> {

    public GestionRepository() {
        super(Gestion.class);
    }

    // JOIN FETCH: trae la gestión junto con su proveedor, pieza y proyecto en UNA sola consulta
    @Override
    public List<Gestion> listarTodos() {
        return consultar(session -> session.createQuery(
                        "SELECT g FROM Gestion g " +
                                "JOIN FETCH g.proveedor JOIN FETCH g.pieza JOIN FETCH g.proyecto " +
                                "ORDER BY g.fecha DESC, g.id DESC", Gestion.class)
                .getResultList());
    }

    public List<Gestion> buscarPorProveedor(String codigoProveedor) {
        return consultar(session -> session.createQuery(
                        "SELECT g FROM Gestion g " +
                                "JOIN FETCH g.proveedor JOIN FETCH g.pieza JOIN FETCH g.proyecto " +
                                "WHERE g.proveedor.codigo = :codigo ORDER BY g.fecha DESC", Gestion.class)
                .setParameter("codigo", codigoProveedor)
                .getResultList());
    }

    public List<Gestion> buscarPorPieza(String codigoPieza) {
        return consultar(session -> session.createQuery(
                        "SELECT g FROM Gestion g " +
                                "JOIN FETCH g.proveedor JOIN FETCH g.pieza JOIN FETCH g.proyecto " +
                                "WHERE g.pieza.codigo = :codigo ORDER BY g.fecha DESC", Gestion.class)
                .setParameter("codigo", codigoPieza)
                .getResultList());
    }

    public List<Gestion> buscarPorProyecto(String codigoProyecto) {
        return consultar(session -> session.createQuery(
                        "SELECT g FROM Gestion g " +
                                "JOIN FETCH g.proveedor JOIN FETCH g.pieza JOIN FETCH g.proyecto " +
                                "WHERE g.proyecto.codigo = :codigo ORDER BY g.fecha DESC", Gestion.class)
                .setParameter("codigo", codigoProyecto)
                .getResultList());
    }
}