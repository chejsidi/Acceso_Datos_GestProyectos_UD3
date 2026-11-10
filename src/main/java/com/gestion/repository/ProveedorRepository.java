package com.gestion.repository;

import com.gestion.model.Proveedor;

import java.util.List;

public class ProveedorRepository extends RepositorioBase<Proveedor, String> {

    public ProveedorRepository() {
        super(Proveedor.class);
    }

    // LIKE con % para buscar por coincidencia parcial; LOWER para ignorar mayúsculas
    public List<Proveedor> buscarPorNombre(String nombre) {
        return consultar(session -> session.createQuery(
                        "SELECT p FROM Proveedor p WHERE LOWER(p.nombre) LIKE LOWER(:texto) ORDER BY p.nombre",
                        Proveedor.class)
                .setParameter("texto", "%" + nombre + "%")
                .getResultList());
    }

    public List<Proveedor> buscarPorDireccion(String direccion) {
        return consultar(session -> session.createQuery(
                        "SELECT p FROM Proveedor p WHERE LOWER(p.direccion) LIKE LOWER(:texto) ORDER BY p.nombre",
                        Proveedor.class)
                .setParameter("texto", "%" + direccion + "%")
                .getResultList());
    }

    // Se usa para impedir borrar un proveedor con suministros (integridad referencial)
    public long contarGestiones(String codigo) {
        return consultar(session -> session.createQuery(
                        "SELECT COUNT(g) FROM Gestion g WHERE g.proveedor.codigo = :codigo", Long.class)
                .setParameter("codigo", codigo)
                .getSingleResult());
    }
}