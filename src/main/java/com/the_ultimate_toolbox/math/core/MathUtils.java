package com.the_ultimate_toolbox.math.core;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Clase de utilidades matemáticas que proporciona operaciones y cálculos matemáticos comunes.
 * Esta clase contiene métodos estáticos para realizar diferentes operaciones matemáticas como
 * encontrar múltiplos, calcular potencias, máximo común divisor y realizar redondeos.
 *
 * <p>Las principales funcionalidades incluyen:
 * <ul>
 *   <li>Cálculo de múltiplos de 10 y 100</li>
 *   <li>Cálculo de potencias</li>
 *   <li>Cálculo del máximo común divisor (MCD)</li>
 *   <li>Redondeo de números decimales</li>
 *   <li>Redondeo a potencias de 10</li>
 * </ul>
 *
 * @author Bufigol
 * @version 1.0
 * @since 1.0
 */
public class MathUtils {

    /**
     * Calcula el siguiente múltiplo de diez más cercano a un número.
     *
     * @param number El número para el cual encontrar el siguiente múltiplo de diez
     * @return El múltiplo de diez más cercano
     */
    public static int nextMultipleOfTen(int number) {
        return (int) (Math.round((number + 5) / 10.0) * 10.0);
    }

    /**
     * Calcula el siguiente múltiplo de cien más cercano a un número.
     *
     * @param number El número para el cual encontrar el siguiente múltiplo de cien
     * @return El múltiplo de cien más cercano
     */
    public static int nextMultipleOfHundred(int number) {
        return (int) (Math.round((number + 50) / 100.0) * 100.0);
    }

    /**
     * Calcula la potencia de un número.
     *
     * @param base La base
     * @param exponent El exponente
     * @return base elevado a exponent
     */
    public static double power(double base, int exponent) {
        if (exponent == 0) {
            return 1;
        } else if (exponent > 0) {
            double result = 1.0;
            for (int i = 0; i < exponent; i++) {
                result *= base;
            }
            return result;
        } else {
            return 1 / power(base, -exponent);
        }
    }

    /**
     * Calcula el máximo común divisor de dos números utilizando el algoritmo de Euclides.
     *
     * @param a Primer número
     * @param b Segundo número
     * @return El máximo común divisor de a y b
     */
    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    /**
     * Redondea un número decimal a un número específico de decimales.
     *
     * @param value El número que se va a redondear
     * @param places El número de decimales
     * @return El número redondeado con el número de decimales especificado
     * @throws IllegalArgumentException Si places es negativo
     */
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException("El número de decimales debe ser mayor o igual a cero");
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Redondea un número al último poder de diez.
     *
     * @param n El número a redondear
     * @return La potencia de diez correspondiente al número de dígitos de n
     */
    public static int roundToLastPowerOfTen(int n) {
        int length = String.valueOf(n).length();
        String stTen = "1";

        for (int i = 0; i < length-1; i++) {
            stTen += "0";
        }

        return Integer.parseInt(stTen);
    }
}