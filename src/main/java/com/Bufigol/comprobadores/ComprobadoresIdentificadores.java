package com.Bufigol.comprobadores;

public class ComprobadoresIdentificadores {
    private static final int[] FACTORES = {2, 3, 4, 5, 6, 7};
    private static final String LETRAS = "TRWAGMYFPDXBNJZSQVHLCKE";


    public static boolean isValidRut(String rut) {
        // Eliminar puntos y guiones
        String rutSinPuntos = rut.replace(".", "").replace("-", "");

        // Verificar que el RUT tenga 9 caracteres
        if (rutSinPuntos.length() != 9) {
            return false;
        }

        // Verificar que el RUT tenga solo números y una K o un número al final
        for (int i = 0; i < 8; i++) {
            if (!Character.isDigit(rutSinPuntos.charAt(i))) {
                return false;
            }
        }
        char ultimoCaracter = rutSinPuntos.charAt(8);
        if (!(Character.isDigit(ultimoCaracter) || ultimoCaracter == 'K')) {
            return false;
        }

        // Calcular el dígito verificador
        int suma = 0;
        for (int i = 0; i < 8; i++) {
            suma += Character.getNumericValue(rutSinPuntos.charAt(i)) * FACTORES[(7 - i) % 6];
        }
        int digitoVerificador = 11 - (suma % 11);
        if (digitoVerificador == 11) {
            digitoVerificador = 0;
        } else if (digitoVerificador == 10) {
            digitoVerificador = 'K';
        }

        // Verificar que el dígito verificador sea igual al último caracter del RUT
        if (Character.isDigit(ultimoCaracter)) {
            return Character.getNumericValue(ultimoCaracter) == digitoVerificador;
        } else {
            return ultimoCaracter == 'K' && digitoVerificador == 'K';
        }
    }
    public static boolean isValidDni(String dni) {
        // Verificar que el DNI tenga 9 caracteres
        if (dni.length() != 9) {
            return false;
        }

        // Verificar que los primeros 8 caracteres sean números
        String numeroStr = dni.substring(0, 8);
        if (!numeroStr.matches("\\d{8}")) {
            return false;
        }

        // Obtener la letra del DNI (último carácter)
        char letraProporcionada = Character.toUpperCase(dni.charAt(8));

        // Calcular la letra correcta
        int numero = Integer.parseInt(numeroStr);
        char letraCalculada = LETRAS.charAt(numero % 23);

        // Verificar que la letra calculada sea igual a la letra del DNI
        return letraCalculada == letraProporcionada;
    }
}