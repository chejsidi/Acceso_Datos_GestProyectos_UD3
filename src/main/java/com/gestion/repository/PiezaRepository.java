package com.gestion.repository;

import com.gestion.model.Pieza;

import java.util.List;

public class PiezaRepository extends RepositorioBase<Pieza, String> {

    public PiezaRepository() {
        super(Pieza.class);
    }

    public List<Pieza> buscarPorNombre(String nombre) {
        return consultar(session -> session.createQuery(
                        "SELECT p FROM Pieza p WHERE LOWER(p.nombre) LIKE LOWER(:texto) ORDER BY p.nombre",
                        Pieza.class)
                .setParameter("texto", "%" + nombre + "%")
                .getResultList());
    }

    public long contarGestiones(String codigo) {
        return consultar(session -> session.createQuery(
                        "SELECT COUNT(g) FROM Gestion g WHERE g.pieza.codigo = :codigo", Long.class)
                .setParameter("codigo", codigo)
                .getSingleResult());
    }
}