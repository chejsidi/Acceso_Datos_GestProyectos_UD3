package com.gestion.repository;

import com.gestion.model.Gestion;
import com.gestion.dto.ConsumoPieza;
import com.gestion.dto.Ranking;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    public List<Gestion> historicoProveedor(String codigo, LocalDate desde, LocalDate hasta) {
        return consultar(session -> session.createQuery("""
                        SELECT g FROM Gestion g
                        JOIN FETCH g.proveedor JOIN FETCH g.pieza JOIN FETCH g.proyecto
                        WHERE g.proveedor.codigo = :codigo AND g.fecha BETWEEN :desde AND :hasta
                        ORDER BY g.fecha""", Gestion.class)
                .setParameter("codigo", codigo)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getResultList());
    }

    public long contarProveedoresDeProyecto(String codigo) {
        return consultar(session -> session.createQuery(
                        "SELECT COUNT(DISTINCT g.proveedor.codigo) FROM Gestion g WHERE g.proyecto.codigo = :codigo",
                        Long.class)
                .setParameter("codigo", codigo)
                .getSingleResult());
    }

    public List<ConsumoPieza> consumoPiezasDeProyecto(String codigo) {
        return consultar(session -> session.createQuery("""
                        SELECT new com.gestion.dto.ConsumoPieza(
                               g.pieza.codigo, g.pieza.nombre, SUM(g.cantidad), SUM(g.cantidad * g.pieza.precio))
                        FROM Gestion g
                        WHERE g.proyecto.codigo = :codigo
                        GROUP BY g.pieza.codigo, g.pieza.nombre
                        ORDER BY SUM(g.cantidad * g.pieza.precio) DESC""", ConsumoPieza.class)
                .setParameter("codigo", codigo)
                .getResultList());
    }

    public long totalUnidades() {
        return consultar(session -> session.createQuery(
                "SELECT COALESCE(SUM(g.cantidad), 0) FROM Gestion g", Number.class).getSingleResult()).longValue();
    }

    public BigDecimal costeTotal() {
        Number total = consultar(session -> session.createQuery(
                "SELECT COALESCE(SUM(g.cantidad * g.pieza.precio), 0) FROM Gestion g", Number.class).getSingleResult());
        return new BigDecimal(total.toString());
    }

    public List<Ranking> proveedoresMasActivos(int limite) {
        return ranking("proveedor", limite);
    }

    public List<Ranking> piezasMasUtilizadas(int limite) {
        return ranking("pieza", limite);
    }

    public List<Ranking> proyectosConMasSuministros(int limite) {
        return ranking("proyecto", limite);
    }

    private List<Ranking> ranking(String relacion, int limite) {
        String jpql = """
                SELECT new com.gestion.dto.Ranking(g.%1$s.codigo, g.%1$s.nombre, COUNT(g), SUM(g.cantidad))
                FROM Gestion g
                GROUP BY g.%1$s.codigo, g.%1$s.nombre
                ORDER BY SUM(g.cantidad) DESC""".formatted(relacion);
        return consultar(session -> session.createQuery(jpql, Ranking.class)
                .setMaxResults(limite)
                .getResultList());
    }
}