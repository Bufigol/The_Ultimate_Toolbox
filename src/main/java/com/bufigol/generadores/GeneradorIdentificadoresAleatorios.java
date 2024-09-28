package com.bufigol.generadores;

import org.jetbrains.annotations.NotNull;

public class GeneradorIdentificadoresAleatorios {
    private static final int[] FACTORES = {2, 3, 4, 5, 6, 7};
    private static final String LETRAS = "TRWAGMYFPDXBNJZSQVHLCKE";

    public static String generarRut() {
        // Generar un número aleatorio de 8 dígitos
        int[] numeros = new int[8];
        for (int i = 0; i < 8; i++) {
            numeros[i] = (int) (Math.random() * 10);
        }

        // Agregar puntos al número
        StringBuilder numeroConPuntos = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (i == 2 || i == 5) {
                numeroConPuntos.append(".");
            }
            numeroConPuntos.append(numeros[i]);
        }

        // Calcular el dígito verificador
        int suma = 0;
        for (int i = 0; i < 8; i++) {
            suma += numeros[i] * FACTORES[(7 - i) % 6]; // Ajuste aquí
        }
        int digitoVerificador = 11 - (suma % 11);
        if (digitoVerificador == 11) {
            digitoVerificador = 0;
        } else if (digitoVerificador == 10) {
            digitoVerificador = 'K';
        }

        // Construir el RUT completo
        StringBuilder rut = new StringBuilder();
        rut.append(numeroConPuntos);
        rut.append("-");
        if (digitoVerificador == 'K') {
            rut.append("K");
        } else {
            rut.append(digitoVerificador);
        }

        return rut.toString();
    }

    public static String generarDni() {
        // Generar un número aleatorio de 8 dígitos
        StringBuilder numeros = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            numeros.append((int) (Math.random() * 10));
        }

        // Calcular la letra del DNI
        int numeroDni = Integer.parseInt(numeros.toString());
        char letra = LETRAS.charAt(numeroDni % 23);

        // Construir el DNI
        return numeros.toString() + letra;
    }
}
