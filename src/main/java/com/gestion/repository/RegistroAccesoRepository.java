package com.gestion.repository;

import com.gestion.model.RegistroAcceso;

import java.util.List;

public class RegistroAccesoRepository extends RepositorioBase<RegistroAcceso, Long> {

    public RegistroAccesoRepository() {
        super(RegistroAcceso.class);
    }

    @Override
    protected boolean auditable() {
        return false;
    }

    public List<RegistroAcceso> ultimos(int cantidad) {
        return consultar(session -> session.createQuery(
                        "SELECT r FROM RegistroAcceso r ORDER BY r.fechaHora DESC", RegistroAcceso.class)
                .setMaxResults(cantidad)
                .getResultList());
    }
}