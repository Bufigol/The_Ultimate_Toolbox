package com.the_ultimate_toolbox.math.core;

/**
 * Proporciona utilidades para operaciones con números.
 */
public class NumberUtils {

    /**
     * Calcula el factorial de un número.
     *
     * @param n El número para calcular su factorial
     * @return El factorial de n
     * @throws IllegalArgumentException Si n es negativo
     */
    public static int factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("El factorial no está definido para números negativos");
        }
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * Multiplica cada elemento de un array por un valor.
     *
     * @param array El array a multiplicar
     * @param n El valor por el cual multiplicar
     * @return Un nuevo array con los elementos multiplicados
     */
    public static double[] multiplyArrayByDouble(double[] array, double n) {
        if (array == null) {
            return null;
        }

        double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i] * n;
        }
        return result;
    }
}