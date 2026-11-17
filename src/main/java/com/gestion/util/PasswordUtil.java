package com.gestion.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

// Hash de contraseñas con PBKDF2 (incluido en Java, recomendado por OWASP)
public class PasswordUtil {

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    // Muchas iteraciones = calcular cada hash es lento, y un ataque de fuerza bruta también
    private static final int ITERACIONES = 600_000;
    private static final int LONGITUD_BITS = 256;
    private static final SecureRandom random = new SecureRandom();

    private PasswordUtil() {
    }

    // Sal aleatoria distinta para cada usuario: dos contraseñas iguales dan hashes distintos
    public static String generarSal() {
        byte[] sal = new byte[16];
        random.nextBytes(sal);
        return Base64.getEncoder().encodeToString(sal);
    }

    public static String hashear(char[] password, String sal) {
        PBEKeySpec spec = new PBEKeySpec(password, Base64.getDecoder().decode(sal), ITERACIONES, LONGITUD_BITS);
        try {
            byte[] hash = SecretKeyFactory.getInstance(ALGORITMO).generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Algoritmo de hash no disponible", e);
        } finally {
            spec.clearPassword();
        }
    }

    // Se recalcula el hash con la misma sal y se compara en tiempo constante
    public static boolean verificar(char[] password, String sal, String hashGuardado) {
        byte[] calculado = Base64.getDecoder().decode(hashear(password, sal));
        return MessageDigest.isEqual(calculado, Base64.getDecoder().decode(hashGuardado));
    }
}