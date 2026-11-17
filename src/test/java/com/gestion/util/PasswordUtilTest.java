package com.gestion.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    @DisplayName("La contraseña correcta se verifica")
    void passwordCorrecta() {
        String sal = PasswordUtil.generarSal();
        String hash = PasswordUtil.hashear("secreto123".toCharArray(), sal);
        assertTrue(PasswordUtil.verificar("secreto123".toCharArray(), sal, hash));
    }

    @Test
    @DisplayName("Una contraseña incorrecta no se verifica")
    void passwordIncorrecta() {
        String sal = PasswordUtil.generarSal();
        String hash = PasswordUtil.hashear("secreto123".toCharArray(), sal);
        assertFalse(PasswordUtil.verificar("secreto124".toCharArray(), sal, hash));
    }

    @Test
    @DisplayName("La misma contraseña con sales distintas da hashes distintos")
    void salDistintaHashDistinto() {
        char[] password = "secreto123".toCharArray();
        String hash1 = PasswordUtil.hashear(password, PasswordUtil.generarSal());
        String hash2 = PasswordUtil.hashear(password, PasswordUtil.generarSal());
        assertNotEquals(hash1, hash2);
    }

    @Test
    @DisplayName("El hash no contiene la contraseña en claro")
    void hashNoContienePassword() {
        String hash = PasswordUtil.hashear("secreto123".toCharArray(), PasswordUtil.generarSal());
        assertFalse(hash.contains("secreto123"));
    }
}