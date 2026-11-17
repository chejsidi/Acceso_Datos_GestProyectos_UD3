package com.gestion.service;

import com.gestion.model.RegistroAcceso;
import com.gestion.model.RegistroOperacion;
import com.gestion.repository.RegistroAccesoRepository;
import com.gestion.repository.RegistroOperacionRepository;

import java.util.List;

public class AuditoriaService {

    private final RegistroAccesoRepository accesoRepository;
    private final RegistroOperacionRepository operacionRepository;

    public AuditoriaService(RegistroAccesoRepository accesoRepository, RegistroOperacionRepository operacionRepository) {
        this.accesoRepository = accesoRepository;
        this.operacionRepository = operacionRepository;
    }

    public List<RegistroAcceso> ultimosAccesos(int cantidad) {
        return accesoRepository.ultimos(cantidad);
    }

    public List<RegistroOperacion> ultimasOperaciones(int cantidad) {
        return operacionRepository.ultimas(cantidad);
    }

    public List<RegistroOperacion> operacionesDeUsuario(String usuario, int cantidad) {
        return operacionRepository.deUsuario(usuario, cantidad);
    }
}