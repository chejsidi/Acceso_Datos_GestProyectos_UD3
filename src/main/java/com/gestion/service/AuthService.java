package com.gestion.service;

import com.gestion.exception.EntidadNoEncontradaException;
import com.gestion.exception.ValidacionException;
import com.gestion.model.RegistroAcceso;
import com.gestion.model.Usuario;
import com.gestion.repository.RegistroAccesoRepository;
import com.gestion.repository.UsuarioRepository;
import com.gestion.util.PasswordUtil;
import com.gestion.util.SesionActual;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UsuarioRepository usuarioRepository;
    private final RegistroAccesoRepository accesoRepository;

    public AuthService(UsuarioRepository usuarioRepository, RegistroAccesoRepository accesoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.accesoRepository = accesoRepository;
    }

    public boolean hayUsuarios() {
        return usuarioRepository.contar() > 0;
    }

    public void crearUsuario(String nombre, char[] password) {
        if (nombre == null || nombre.isBlank()) {
            throw new ValidacionException("El nombre de usuario es obligatorio.");
        }
        validarPassword(password);
        if (usuarioRepository.existe(nombre)) {
            throw new ValidacionException("Ya existe el usuario " + nombre + ".");
        }
        String sal = PasswordUtil.generarSal();
        usuarioRepository.guardar(new Usuario(nombre, PasswordUtil.hashear(password, sal), sal));
        log.info("Usuario creado: {}", nombre);
    }

    // Todos los intentos, correctos o no, quedan en el registro de accesos
    public boolean login(String nombre, char[] password) {
        Optional<Usuario> usuario = usuarioRepository.buscarPorId(nombre);
        boolean correcto = usuario.isPresent()
                && PasswordUtil.verificar(password, usuario.get().getSal(), usuario.get().getHash());

        accesoRepository.guardar(new RegistroAcceso(nombre, correcto));
        if (correcto) {
            SesionActual.iniciar(nombre);
            log.info("Inicio de sesión: {}", nombre);
        } else {
            log.warn("Intento de acceso fallido: {}", nombre);
        }
        return correcto;
    }

    public void cambiarPassword(char[] actual, char[] nueva) {
        Usuario usuario = usuarioRepository.buscarPorId(SesionActual.nombreUsuario())
                .orElseThrow(() -> new EntidadNoEncontradaException("No hay ninguna sesión iniciada."));
        if (!PasswordUtil.verificar(actual, usuario.getSal(), usuario.getHash())) {
            throw new ValidacionException("La contraseña actual no es correcta.");
        }
        validarPassword(nueva);
        // Nueva sal en cada cambio de contraseña
        String sal = PasswordUtil.generarSal();
        usuario.setSal(sal);
        usuario.setHash(PasswordUtil.hashear(nueva, sal));
        usuarioRepository.actualizar(usuario);
        log.info("Contraseña cambiada: {}", usuario.getNombre());
    }

    private void validarPassword(char[] password) {
        boolean tieneLetra = false;
        boolean tieneNumero = false;
        for (char c : password) {
            if (Character.isLetter(c)) tieneLetra = true;
            if (Character.isDigit(c)) tieneNumero = true;
        }
        if (password.length < 8 || !tieneLetra || !tieneNumero) {
            throw new ValidacionException("La contraseña debe tener al menos 8 caracteres, con letras y números.");
        }
    }
}