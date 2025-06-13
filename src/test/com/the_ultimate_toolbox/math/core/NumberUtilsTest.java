package com.the_ultimate_toolbox.math.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the class {@link NumberUtils}
 */
public class NumberUtilsTest {


    @ParameterizedTest
    @CsvSource({
            "0, 1",
            "1, 1",
            "2, 2",
            "3, 6",
            "10, 3628800"
    })
    @DisplayName("Prueba de factorial")
    void testFactorialCorrecto(int input, int expected) {
        assertEquals(expected, NumberUtils.factorial(input));
    }

    @Test
    @DisplayName("factorial debe lanzar IllegalArgumentException para números negativos")
    void testFactorialNegativo() {
        // Test con un número negativo simple
        IllegalArgumentException exception1 = assertThrows(
                IllegalArgumentException.class,
                () -> NumberUtils.factorial(-1),
                "Debería lanzar IllegalArgumentException para -1"
        );
        assertEquals("Factorial is not defined for negative numbers", exception1.getMessage());

        // Test con un número negativo más grande
        IllegalArgumentException exception2 = assertThrows(
                IllegalArgumentException.class,
                () -> NumberUtils.factorial(-5),
                "Debería lanzar IllegalArgumentException para -5"
        );
        assertEquals("Factorial is not defined for negative numbers", exception2.getMessage());

        // Test con un número negativo muy grande
        assertThrows(
                IllegalArgumentException.class,
                () -> NumberUtils.factorial(-100),
                "Debería lanzar IllegalArgumentException para -100"
        );
    }
    @Test
    @DisplayName("factorial debe lanzar IllegalArgumentException para números negativos")
    void testFactorialNegativoSimpler() {
        assertThrows(IllegalArgumentException.class, () -> NumberUtils.factorial(-1));
    }


    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -5, -10, -100})
    @DisplayName("factorial debe lanzar IllegalArgumentException para cualquier número negativo")
    void testFactorialNegativo(int numeroNegativo) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> NumberUtils.factorial(numeroNegativo),
                "Debería lanzar IllegalArgumentException para " + numeroNegativo
        );
        assertEquals("Factorial is not defined for negative numbers", exception.getMessage());
    }

    @Test
    @DisplayName("Test de nulos para multiplicacion de arrays por constante")
    void testArrayNulo() {
        assertNull(NumberUtils.multiplyArrayByDouble(null, 5));
    }

    @Test
    @DisplayName("Test basico")
    void testMultiplicacionArray(){
        double[] base = {1.0, 2.0,3.0,4.0};
        int multiplicador = 2;
        double[] resultado = NumberUtils.multiplyArrayByDouble(base,multiplicador);
        double[] resultadosEsperados = {2.0, 4.0 , 6.0 ,8.0};
        for (int i = 0; i < base.length; i++){
            assertEquals(resultadosEsperados[i],resultado[i]);
        }
    }

    @Test
    @DisplayName("Test con array vacío")
    void testArrayVacio() {
        double[] arrayVacio = {};
        double[] resultado = NumberUtils.multiplyArrayByDouble(arrayVacio, 5.0);

        assertNotNull(resultado);
        assertEquals(0, resultado.length);
    }

    @Test
    @DisplayName("Test con array de un solo elemento")
    void testArrayUnElemento() {
        double[] array = {7.5};
        double[] resultado = NumberUtils.multiplyArrayByDouble(array, 3.0);
        double[] esperado = {22.5};

        assertArrayEquals(esperado, resultado, 0.0001);
    }

    @Test
    @DisplayName("Test básico de multiplicación")
    void testMultiplicacionBasica() {
        double[] base = {1.0, 2.0, 3.0, 4.0};
        double multiplicador = 2.0;
        double[] resultado = NumberUtils.multiplyArrayByDouble(base, multiplicador);
        double[] esperado = {2.0, 4.0, 6.0, 8.0};

        assertArrayEquals(esperado, resultado, 0.0001);
    }

    @Test
    @DisplayName("Test multiplicación por cero")
    void testMultiplicacionPorCero() {
        double[] base = {1.5, -2.3, 4.7, -8.1};
        double[] resultado = NumberUtils.multiplyArrayByDouble(base, 0.0);
        double[] esperado = {0.0, 0.0, 0.0, 0.0};

        assertArrayEquals(esperado, resultado, 0.0001);
    }

    @Test
    @DisplayName("Test multiplicación por uno")
    void testMultiplicacionPorUno() {
        double[] base = {3.14, -2.71, 1.41, 0.0};
        double[] resultado = NumberUtils.multiplyArrayByDouble(base, 1.0);

        assertArrayEquals(base, resultado, 0.0001);
    }

    // ==================== TESTS CON NÚMEROS NEGATIVOS ====================

    @Test
    @DisplayName("Test multiplicación por número negativo")
    void testMultiplicacionPorNegativo() {
        double[] base = {2.0, -3.0, 4.0, -5.0};
        double[] resultado = NumberUtils.multiplyArrayByDouble(base, -2.0);
        double[] esperado = {-4.0, 6.0, -8.0, 10.0};

        assertArrayEquals(esperado, resultado, 0.0001);
    }

    @Test
    @DisplayName("Test con array de números negativos")
    void testArrayNumerosNegativos() {
        double[] base = {-1.0, -2.0, -3.0, -4.0};
        double[] resultado = NumberUtils.multiplyArrayByDouble(base, 3.0);
        double[] esperado = {-3.0, -6.0, -9.0, -12.0};

        assertArrayEquals(esperado, resultado, 0.0001);
    }

    // ==================== TESTS CON NÚMEROS DECIMALES ====================

    @Test
    @DisplayName("Test con números decimales")
    void testNumerosDecimales() {
        double[] base = {1.5, 2.25, 3.75, 4.125};
        double[] resultado = NumberUtils.multiplyArrayByDouble(base, 2.5);
        double[] esperado = {3.75, 5.625, 9.375, 10.3125};

        assertArrayEquals(esperado, resultado, 0.0001);
    }

    @Test
    @DisplayName("Test multiplicación por decimal pequeño")
    void testMultiplicacionDecimalPequeño() {
        double[] base = {10.0, 20.0, 30.0};
        double[] resultado = NumberUtils.multiplyArrayByDouble(base, 0.1);
        double[] esperado = {1.0, 2.0, 3.0};

        assertArrayEquals(esperado, resultado, 0.0001);
    }

    // ==================== TESTS CON CASOS ESPECIALES ====================

    @Test
    @DisplayName("Test con infinito positivo")
    void testInfinitoPositivo() {
        double[] base = {1.0, 2.0, 3.0};
        double[] resultado = NumberUtils.multiplyArrayByDouble(base, Double.POSITIVE_INFINITY);

        assertEquals(Double.POSITIVE_INFINITY, resultado[0]);
        assertEquals(Double.POSITIVE_INFINITY, resultado[1]);
        assertEquals(Double.POSITIVE_INFINITY, resultado[2]);
    }

    @Test
    @DisplayName("Test con infinito negativo")
    void testInfinitoNegativo() {
        double[] base = {1.0, -2.0, 3.0};
        double[] resultado = NumberUtils.multiplyArrayByDouble(base, Double.NEGATIVE_INFINITY);

        assertEquals(Double.NEGATIVE_INFINITY, resultado[0]);
        assertEquals(Double.POSITIVE_INFINITY, resultado[1]); // -2 * -∞ = +∞
        assertEquals(Double.NEGATIVE_INFINITY, resultado[2]);
    }

    @Test
    @DisplayName("Test con NaN")
    void testConNaN() {
        double[] base = {1.0, 2.0, 3.0};
        double[] resultado = NumberUtils.multiplyArrayByDouble(base, Double.NaN);

        assertTrue(Double.isNaN(resultado[0]));
        assertTrue(Double.isNaN(resultado[1]));
        assertTrue(Double.isNaN(resultado[2]));
    }

    @Test
    @DisplayName("Test con array que contiene NaN")
    void testArrayContieneNaN() {
        double[] base = {1.0, Double.NaN, 3.0};
        double[] resultado = NumberUtils.multiplyArrayByDouble(base, 2.0);

        assertEquals(2.0, resultado[0], 0.0001);
        assertTrue(Double.isNaN(resultado[1]));
        assertEquals(6.0, resultado[2], 0.0001);
    }

    @Test
    @DisplayName("Test con array que contiene infinito")
    void testArrayContieneInfinito() {
        double[] base = {1.0, Double.POSITIVE_INFINITY, -3.0};
        double[] resultado = NumberUtils.multiplyArrayByDouble(base, 2.0);

        assertEquals(2.0, resultado[0], 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, resultado[1]);
        assertEquals(-6.0, resultado[2], 0.0001);
    }

    @Test
    @DisplayName("Test con ceros positivos y negativos")
    void testCerosPositivosNegativos() {
        double[] base = {0.0, -0.0, 1.0};
        double[] resultado = NumberUtils.multiplyArrayByDouble(base, 5.0);
        double[] esperado = {0.0, -0.0, 5.0};

        assertEquals(0.0, resultado[0]);
        assertEquals(-0.0, resultado[1]);
        assertEquals(5.0, resultado[2], 0.0001);
    }

    // ==================== TESTS DE RENDIMIENTO Y ARRAYS GRANDES ====================

    @Test
    @DisplayName("Test con array grande")
    void testArrayGrande() {
        int tamaño = 1000;
        double[] base = new double[tamaño];
        double[] esperado = new double[tamaño];

        // Llenar arrays
        for (int i = 0; i < tamaño; i++) {
            base[i] = i + 1;
            esperado[i] = (i + 1) * 3.0;
        }

        double[] resultado = NumberUtils.multiplyArrayByDouble(base, 3.0);

        assertArrayEquals(esperado, resultado, 0.0001);
    }

    // ==================== TESTS DE INMUTABILIDAD ====================

    @Test
    @DisplayName("Test que el array original no se modifica")
    void testArrayOriginalNoSeModifica() {
        double[] original = {1.0, 2.0, 3.0, 4.0};
        double[] copia = original.clone();

        NumberUtils.multiplyArrayByDouble(original, 5.0);

        assertArrayEquals(copia, original, 0.0001);
    }

    // ==================== TESTS PARAMETRIZADOS ====================

    @ParameterizedTest
    @CsvSource({
            "1.0, 2.0",
            "2.5, 4.0",
            "-3.0, 2.0",
            "0.0, 10.0",
            "100.0, 0.01"
    })
    @DisplayName("Test parametrizado con diferentes multiplicadores")
    void testParametrizadoMultiplicadores(double valorBase, double multiplicador) {
        double[] base = {valorBase};
        double[] resultado = NumberUtils.multiplyArrayByDouble(base, multiplicador);
        double esperado = valorBase * multiplicador;

        assertEquals(esperado, resultado[0], 0.0001);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-100.0, -1.0, 0.0, 1.0, 2.5, 100.0, Double.MAX_VALUE})
    @DisplayName("Test con valores extremos como multiplicadores")
    void testValoresExtremos(double multiplicador) {
        double[] base = {1.0, 2.0, 3.0};
        double[] resultado = NumberUtils.multiplyArrayByDouble(base, multiplicador);

        assertNotNull(resultado);
        assertEquals(3, resultado.length);

        if (!Double.isInfinite(multiplicador) && !Double.isNaN(multiplicador)) {
            assertEquals(1.0 * multiplicador, resultado[0], 0.0001);
            assertEquals(2.0 * multiplicador, resultado[1], 0.0001);
            assertEquals(3.0 * multiplicador, resultado[2], 0.0001);
        }
    }

    /**
     * Checks if a given number is prime.
     * <p>
     * A prime number is a natural number greater than 1 that is not a product of
     * two smaller natural numbers.
     * </p>
     *
     * @param n The number to check for primality
     * @return true if the number is prime, false otherwise
     */
    public static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        if (n <= 3) {
            return true;
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }

        int i = 5;
        while (i * i <= n) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
            i += 6;
        }
        return true;
    }

    /**
     * Formats a number as a string with the specified format.
     * <p>
     * This method provides a convenient way to format numbers according to
     * common patterns.
     * </p>
     *
     * @param value The number to format
     * @param format The format string (e.g., "%.2f" for 2 decimal places)
     * @return The formatted string representation of the number
     */
    public static String format(double value, String format) {
        return String.format(format, value);
    }
}
