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

    public List<Proyecto> busquedaAvanzada(String nombre, String ciudad, String codigoProveedor, String codigoPieza) {
        return consultar(session -> session.createQuery("""
                        SELECT p FROM Proyecto p
                        WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE :nombre)
                          AND (:ciudad IS NULL OR LOWER(p.ciudad) LIKE :ciudad)
                          AND (:proveedor IS NULL OR EXISTS (
                                SELECT g1 FROM Gestion g1 WHERE g1.proyecto = p AND g1.proveedor.codigo = :proveedor))
                          AND (:pieza IS NULL OR EXISTS (
                                SELECT g2 FROM Gestion g2 WHERE g2.proyecto = p AND g2.pieza.codigo = :pieza))
                        ORDER BY p.nombre""", Proyecto.class)
                .setParameter("nombre", patronLike(nombre))
                .setParameter("ciudad", patronLike(ciudad))
                .setParameter("proveedor", codigoProveedor)
                .setParameter("pieza", codigoPieza)
                .getResultList());
    }
}