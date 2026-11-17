package com.gestion.repository;

import com.gestion.model.RegistroOperacion;

import java.util.List;

public class RegistroOperacionRepository extends RepositorioBase<RegistroOperacion, Long> {

    public RegistroOperacionRepository() {
        super(RegistroOperacion.class);
    }

    @Override
    protected boolean auditable() {
        return false;
    }

    public List<RegistroOperacion> ultimas(int cantidad) {
        return consultar(session -> session.createQuery(
                        "SELECT r FROM RegistroOperacion r ORDER BY r.fechaHora DESC", RegistroOperacion.class)
                .setMaxResults(cantidad)
                .getResultList());
    }

    public List<RegistroOperacion> deUsuario(String usuario, int cantidad) {
        return consultar(session -> session.createQuery(
                        "SELECT r FROM RegistroOperacion r WHERE r.usuario = :usuario ORDER BY r.fechaHora DESC",
                        RegistroOperacion.class)
                .setParameter("usuario", usuario)
                .setMaxResults(cantidad)
                .getResultList());
    }
}