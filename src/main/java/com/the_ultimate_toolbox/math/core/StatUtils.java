package com.the_ultimate_toolbox.math.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Proporciona funciones estadísticas y de análisis de datos.
 */
public class StatUtils {

    /**
     * Calcula el coeficiente binomial (n k).
     *
     * @param n Valor superior del coeficiente binomial
     * @param k Valor inferior del coeficiente binomial
     * @return El coeficiente binomial (n k)
     * @throws IllegalArgumentException Si k es negativo o mayor que n
     */
    public static int binomialCoefficient(int n, int k) {
        if (k < 0 || k > n) {
            throw new IllegalArgumentException("k debe estar en el rango [0, n]");
        }

        if (k == 0 || k == n) {
            return 1;
        }

        // Usar el menor valor para optimizar el cálculo
        k = Math.min(k, n - k);

        int result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (n - (i - 1)) / i;
        }

        return result;
    }

    /**
     * Calcula la distribución binomial para n ensayos y probabilidad p.
     *
     * @param n Número de ensayos
     * @param p Probabilidad de éxito en cada ensayo
     * @return Un array con los valores de la distribución binomial
     * @throws IllegalArgumentException Si n es negativo o p está fuera del rango [0,1]
     */
    public static double[] binomialDistribution(int n, double p) {
        if (n < 0) {
            throw new IllegalArgumentException("n debe ser mayor o igual a cero");
        }
        if (p < 0 || p > 1) {
            throw new IllegalArgumentException("p debe estar en el rango [0,1]");
        }

        double[] dist = new double[n+1];
        for (int x = 0; x <= n; x++) {
            dist[x] = binomialCoefficient(n, x) * Math.pow(p, x) * Math.pow(1-p, n-x);
        }
        return dist;
    }

    /**
     * Encuentra el índice del valor más pequeño en una lista de números.
     *
     * @param list Lista de números
     * @return El índice del valor más pequeño
     * @throws IllegalArgumentException Si la lista es nula o vacía
     */
    public static int findSmallest(List<? extends Number> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("La lista no puede ser nula o vacía");
        }

        int index = 0;
        for (int i = 1; i < list.size(); i++) {
            double atI = list.get(i).doubleValue();
            double atIndex = list.get(index).doubleValue();
            if (atI < atIndex) {
                index = i;
            }
        }
        return index;
    }

    /**
     * Encuentra el índice del valor más grande en una lista de enteros.
     *
     * @param list Lista de enteros
     * @return El índice del valor más grande
     * @throws IllegalArgumentException Si la lista es nula o vacía
     */
    public static int findBiggest(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("La lista no puede ser nula o vacía");
        }

        int index = 0;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) > list.get(index)) {
                index = i;
            }
        }
        return index;
    }

    /**
     * Genera una lista con todos los enteros en un rango.
     *
     * @param start Límite inferior del rango (inclusivo)
     * @param end Límite superior del rango (inclusivo)
     * @return Una lista que contiene todos los enteros entre start y end
     * @throws IllegalArgumentException Si end es menor que start
     */
    public static List<Integer> getAllIntInRange(int start, int end) {
        if (end < start) {
            throw new IllegalArgumentException("El límite superior debe ser mayor o igual que el límite inferior");
        }

        List<Integer> ints = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            ints.add(i);
        }
        return ints;
    }
}