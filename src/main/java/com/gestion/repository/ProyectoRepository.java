package com.gestion.repository;

import com.gestion.model.Proyecto;

import java.util.List;

public class ProyectoRepository extends RepositorioBase<Proyecto, String> {

    public ProyectoRepository() {
        super(Proyecto.class);
    }

    public List<Proyecto> buscarPorNombre(String nombre) {
        return consultar(session -> session.createQuery(
                        "SELECT p FROM Proyecto p WHERE LOWER(p.nombre) LIKE LOWER(:texto) ORDER BY p.nombre",
                        Proyecto.class)
                .setParameter("texto", "%" + nombre + "%")
                .getResultList());
    }

    public List<Proyecto> buscarPorCiudad(String ciudad) {
        return consultar(session -> session.createQuery(
                        "SELECT p FROM Proyecto p WHERE LOWER(p.ciudad) LIKE LOWER(:texto) ORDER BY p.nombre",
                        Proyecto.class)
                .setParameter("texto", "%" + ciudad + "%")
                .getResultList());
    }

    public long contarGestiones(String codigo) {
        return consultar(session -> session.createQuery(
                        "SELECT COUNT(g) FROM Gestion g WHERE g.proyecto.codigo = :codigo", Long.class)
                .setParameter("codigo", codigo)
                .getSingleResult());
    }
}