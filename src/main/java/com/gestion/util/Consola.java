package com.gestion.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.NoSuchElementException;
import java.util.Scanner;

// Lectura de datos por teclado con validación: repite la pregunta hasta que el dato sea correcto
public class Consola {

    private static final Scanner scanner = new Scanner(System.in);


    public static final DateTimeFormatter FORMATO_FECHA =
            // fechas imposibles se rechacen
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private static final String PATRON_CODIGO = "^[A-Za-z0-9]{1,10}$";

    private Consola() {
    }

    // Punto único de lectura: si se cierra la entrada salir de forma controlada
    private static String leerLinea(String mensaje) {
        System.out.print(mensaje);
        try {
            return scanner.nextLine().trim();
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println("\nEntrada cerrada. Saliendo de la aplicación...");
            HibernateUtil.cerrar();
            System.exit(0);
            return "";
        }
    }

    private static void error(String mensaje) {
        System.out.println("  ✗ " + mensaje);
    }

    public static String leerTexto(String mensaje, int longitudMaxima) {
        while (true) {
            String texto = leerLinea(mensaje);
            if (texto.isEmpty()) {
                error("El campo no puede estar vacío.");
            } else if (texto.length() > longitudMaxima) {
                error("Máximo " + longitudMaxima + " caracteres.");
            } else {
                return texto;
            }
        }
    }

    // Para modificaciones: si el usuario pulsa Enter se mantiene el valor actual
    public static String leerTextoOpcional(String mensaje, String valorActual, int longitudMaxima) {
        while (true) {
            String texto = leerLinea(mensaje + " [" + valorActual + "]: ");
            if (texto.isEmpty()) {
                return valorActual;
            } else if (texto.length() > longitudMaxima) {
                error("Máximo " + longitudMaxima + " caracteres.");
            } else {
                return texto;
            }
        }
    }

    // Códigos alfanuméricos: se guardan en mayúsculas para evitar duplicados
    public static String leerCodigo(String mensaje) {
        while (true) {
            String codigo = leerLinea(mensaje);
            if (codigo.matches(PATRON_CODIGO)) {
                return codigo.toUpperCase();
            }
            error("Código no válido: solo letras y números, entre 1 y 10 caracteres (ej: PR001).");
        }
    }

    public static int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            try {
                int numero = Integer.parseInt(leerLinea(mensaje));
                if (numero >= minimo && numero <= maximo) {
                    return numero;
                }
                error("Debe estar entre " + minimo + " y " + maximo + ".");
            } catch (NumberFormatException e) {
                error("Debes introducir un número entero.");
            }
        }
    }

    public static Integer leerEnteroOpcional(String mensaje, int valorActual, int minimo, int maximo) {
        while (true) {
            String texto = leerLinea(mensaje + " [" + valorActual + "]: ");
            if (texto.isEmpty()) {
                return valorActual;
            }
            try {
                int numero = Integer.parseInt(texto);
                if (numero >= minimo && numero <= maximo) {
                    return numero;
                }
                error("Debe estar entre " + minimo + " y " + maximo + ".");
            } catch (NumberFormatException e) {
                error("Debes introducir un número entero.");
            }
        }
    }

    // Acepta coma o punto como separador decimal
    public static BigDecimal leerPrecio(String mensaje) {
        while (true) {
            BigDecimal precio = convertirPrecio(leerLinea(mensaje));
            if (precio != null) {
                return precio;
            }
        }
    }

    public static BigDecimal leerPrecioOpcional(String mensaje, BigDecimal valorActual) {
        while (true) {
            String texto = leerLinea(mensaje + " [" + valorActual + "]: ");
            if (texto.isEmpty()) {
                return valorActual;
            }
            BigDecimal precio = convertirPrecio(texto);
            if (precio != null) {
                return precio;
            }
        }
    }

    // Devuelve null si el texto no es un precio válido (y muestra el motivo)
    private static BigDecimal convertirPrecio(String texto) {
        try {
            BigDecimal precio = new BigDecimal(texto.replace(",", "."));
            if (precio.compareTo(BigDecimal.ZERO) < 0) {
                error("El precio no puede ser negativo.");
            } else if (precio.scale() > 2) {
                error("Máximo 2 decimales.");
            } else if (precio.compareTo(new BigDecimal("99999999.99")) > 0) {
                error("El precio es demasiado alto.");
            } else {
                return precio;
            }
        } catch (NumberFormatException e) {
            error("Debes introducir un número (ej: 12,50).");
        }
        return null;
    }

    // Enter = fecha de hoy
    public static LocalDate leerFecha(String mensaje) {
        while (true) {
            String texto = leerLinea(mensaje + " (dd/mm/aaaa, Enter = hoy): ");
            if (texto.isEmpty()) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(texto, FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                error("Fecha no válida. Formato: dd/mm/aaaa (ej: 05/11/2026).");
            }
        }
    }

    public static LocalDate leerFechaOpcional(String mensaje, LocalDate valorActual) {
        while (true) {
            String texto = leerLinea(mensaje + " [" + valorActual.format(FORMATO_FECHA) + "]: ");
            if (texto.isEmpty()) {
                return valorActual;
            }
            try {
                return LocalDate.parse(texto, FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                error("Fecha no válida. Formato: dd/mm/aaaa (ej: 05/11/2026).");
            }
        }
    }

    public static boolean confirmar(String mensaje) {
        while (true) {
            String respuesta = leerLinea(mensaje + " (s/n): ").toLowerCase();
            if (respuesta.equals("s")) return true;
            if (respuesta.equals("n")) return false;
            error("Responde 's' o 'n'.");
        }
    }

    public static void pausa() {
        leerLinea("\nPulsa Enter para continuar...");
    }
}