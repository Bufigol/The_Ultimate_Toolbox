package com.the_ultimate_toolbox.math.core;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link MathUtils}.
 */
public class MathUtilsTest {

    @ParameterizedTest
    @CsvSource({
            "4, 0",
            "5, 10",
            "14, 10",
            "15, 20",
            "25, 30",
            "-5, -10",
            "-15, -20",
            "0, 0"
    })
    @DisplayName("nextMultipleOfTen debe redondear al múltiplo de 10 más cercano")
    void testNextMultipleOfTen(int input, int expected) {
        assertEquals(expected, MathUtils.nextMultipleOfTen(input));
    }

    @ParameterizedTest
    @CsvSource({
            "40, 0",
            "50, 100",
            "149, 100",
            "150, 200",
            "250, 300",
            "-50, -100",
            "-150, -200",
            "0, 0"
    })
    @DisplayName("nextMultipleOfHundred debe redondear al múltiplo de 100 más cercano")
    void testNextMultipleOfHundred(int input, int expected) {
        assertEquals(expected, MathUtils.nextMultipleOfHundred(input));
    }

    @ParameterizedTest
    @CsvSource({
            "2, 3, 8",
            "5, 2, 25",
            "10, 0, 1",
            "2.5, 2, 6.25",
            "3, -2, 0.11111111111111", // 1/9 aproximadamente
            "0, 5, 0",
            "1, 10, 1"
    })
    @DisplayName("power debe calcular correctamente la potencia")
    void testPower(double base, int exponent, double expected) {
        assertEquals(expected, MathUtils.power(base, exponent), 0.00001);
    }

    @ParameterizedTest
    @CsvSource({
            "48, 18, 6",
            "17, 5, 1",
            "0, 5, 5",
            "5, 0, 5",
            "0, 0, 0",
            "-48, 18, 6",
            "48, -18, 6",
            "-48, -18, 6"
    })
    @DisplayName("gcd debe calcular el máximo común divisor correctamente")
    void testGcd(int a, int b, int expected) {
        assertEquals(expected, MathUtils.gcd(a, b));
    }

    @ParameterizedTest
    @CsvSource({
            "3.14159, 2, 3.14",
            "3.14159, 4, 3.1416",
            "3.5, 0, 4",
            "-3.14159, 2, -3.14",
            "0, 2, 0"
    })
    @DisplayName("round debe redondear a los decimales especificados")
    void testRound(double value, int places, double expected) {
        assertEquals(expected, MathUtils.round(value, places));
    }

    @Test
    @DisplayName("round debe lanzar IllegalArgumentException cuando places es negativo")
    void testRoundWithNegativePlaces() {
        assertThrows(IllegalArgumentException.class, () -> MathUtils.round(3.14, -1));
    }

    @ParameterizedTest
    @CsvSource({
            "5, 1",
            "42, 10",
            "789, 100",
            "1000, 1000",
            "1234, 1000",
            "0, 1",
            "-789, 100"
    })
    @DisplayName("roundToLastPowerOfTen debe encontrar la potencia de diez correcta")
    void testRoundToLastPowerOfTen(int input, int expected) {
        assertEquals(expected, MathUtils.roundToLastPowerOfTen(input));
    }

    @ParameterizedTest
    @CsvSource({
            "1.0, 1.0, 0.00001, true",
            "1.0, 1.0001, 0.001, true",
            "1.0, 1.1, 0.001, false",
            "3.14159, 3.14160, 0.0001, true",
            "-1.0, -1.0001, 0.001, true"
    })
    @DisplayName("equals debe comparar números flotantes con tolerancia")
    void testEquals(double a, double b, double epsilon, boolean expected) {
        assertEquals(expected, MathUtils.equals(a, b, epsilon));
    }

    @Test
    @DisplayName("equals debe lanzar IllegalArgumentException cuando epsilon es negativo")
    void testEqualsWithNegativeEpsilon() {
        assertThrows(IllegalArgumentException.class, () -> MathUtils.equals(1.0, 1.0, -0.1));
    }

    @ParameterizedTest
    @CsvSource({
            "5, 0, 10, 5",
            "-5, 0, 10, 0",
            "15, 0, 10, 10",
            "3.5, 1.0, 4.0, 3.5",
            "0.5, 1.0, 4.0, 1.0",
            "5.0, 5.0, 5.0, 5.0"
    })
    @DisplayName("clamp debe restringir valores al rango especificado")
    void testClamp(double value, double min, double max, double expected) {
        assertEquals(expected, MathUtils.clamp(value, min, max));
    }

    @Test
    @DisplayName("clamp debe lanzar IllegalArgumentException cuando min > max")
    void testClampWithInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> MathUtils.clamp(5, 10, 5));
    }
}