package com.gestion.repository;

import com.gestion.model.Pieza;
import java.math.BigDecimal;
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

    public List<Pieza> busquedaAvanzada(String nombre, BigDecimal precioMinimo, BigDecimal precioMaximo,
                                        String codigoProveedor, String codigoProyecto) {
        return consultar(session -> session.createQuery("""
                        SELECT p FROM Pieza p
                        WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE :nombre)
                          AND (:minimo IS NULL OR p.precio >= :minimo)
                          AND (:maximo IS NULL OR p.precio <= :maximo)
                          AND (:proveedor IS NULL OR EXISTS (
                                SELECT g1 FROM Gestion g1 WHERE g1.pieza = p AND g1.proveedor.codigo = :proveedor))
                          AND (:proyecto IS NULL OR EXISTS (
                                SELECT g2 FROM Gestion g2 WHERE g2.pieza = p AND g2.proyecto.codigo = :proyecto))
                        ORDER BY p.precio""", Pieza.class)
                .setParameter("nombre", patronLike(nombre))
                .setParameter("minimo", precioMinimo)
                .setParameter("maximo", precioMaximo)
                .setParameter("proveedor", codigoProveedor)
                .setParameter("proyecto", codigoProyecto)
                .getResultList());
    }
}