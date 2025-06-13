package com.the_ultimate_toolbox.math.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests completos para la clase StatUtils
 */
public class StatUtilsTest {

    // ==================== TESTS PARA binomialCoefficient ====================

    // ---------- Tests de Casos Base ----------

    @Test
    @DisplayName("binomialCoefficient casos base: (n,0) y (n,n) deben retornar 1")
    void testBinomialCoefficientCasosBase() {
        assertEquals(1, StatUtils.binomialCoefficient(0, 0));
        assertEquals(1, StatUtils.binomialCoefficient(1, 0));
        assertEquals(1, StatUtils.binomialCoefficient(1, 1));
        assertEquals(1, StatUtils.binomialCoefficient(5, 0));
        assertEquals(1, StatUtils.binomialCoefficient(5, 5));
        assertEquals(1, StatUtils.binomialCoefficient(10, 0));
        assertEquals(1, StatUtils.binomialCoefficient(10, 10));
        assertEquals(1, StatUtils.binomialCoefficient(100, 0));
        assertEquals(1, StatUtils.binomialCoefficient(100, 100));
    }

    @Test
    @DisplayName("binomialCoefficient casos simples conocidos")
    void testBinomialCoefficientCasosSimples() {
        assertEquals(2, StatUtils.binomialCoefficient(2, 1));
        assertEquals(3, StatUtils.binomialCoefficient(3, 1));
        assertEquals(3, StatUtils.binomialCoefficient(3, 2));
        assertEquals(4, StatUtils.binomialCoefficient(4, 1));
        assertEquals(6, StatUtils.binomialCoefficient(4, 2));
        assertEquals(4, StatUtils.binomialCoefficient(4, 3));
        assertEquals(5, StatUtils.binomialCoefficient(5, 1));
        assertEquals(10, StatUtils.binomialCoefficient(5, 2));
        assertEquals(10, StatUtils.binomialCoefficient(5, 3));
        assertEquals(5, StatUtils.binomialCoefficient(5, 4));
    }

    @ParameterizedTest
    @CsvSource({
            "6, 1, 6",
            "6, 2, 15",
            "6, 3, 20",
            "6, 4, 15",
            "6, 5, 6",
            "7, 1, 7",
            "7, 2, 21",
            "7, 3, 35",
            "7, 4, 35",
            "8, 2, 28",
            "8, 3, 56",
            "8, 4, 70",
            "9, 2, 36",
            "9, 3, 84",
            "9, 4, 126",
            "10, 1, 10",
            "10, 2, 45",
            "10, 3, 120",
            "10, 4, 210",
            "10, 5, 252"
    })
    @DisplayName("binomialCoefficient casos parametrizados conocidos")
    void testBinomialCoefficientParametrizados(int n, int k, int esperado) {
        assertEquals(esperado, StatUtils.binomialCoefficient(n, k));
    }

    @Test
    @DisplayName("binomialCoefficient simetría: C(n,k) = C(n,n-k)")
    void testBinomialCoefficientSimetria() {
        for (int n = 0; n <= 15; n++) {
            for (int k = 0; k <= n; k++) {
                assertEquals(StatUtils.binomialCoefficient(n, k),
                        StatUtils.binomialCoefficient(n, n - k),
                        String.format("C(%d,%d) debería igual C(%d,%d)", n, k, n, n-k));
            }
        }
    }

    @Test
    @DisplayName("binomialCoefficient identidad de Pascal: C(n,k) = C(n-1,k-1) + C(n-1,k)")
    void testBinomialCoefficientIdentidadPascal() {
        for (int n = 2; n <= 15; n++) {
            for (int k = 1; k < n; k++) {
                int left = StatUtils.binomialCoefficient(n - 1, k - 1);
                int right = StatUtils.binomialCoefficient(n - 1, k);
                int expected = StatUtils.binomialCoefficient(n, k);
                assertEquals(expected, left + right,
                        String.format("C(%d,%d) = C(%d,%d) + C(%d,%d)", n, k, n-1, k-1, n-1, k));
            }
        }
    }

    @Test
    @DisplayName("binomialCoefficient números grandes")
    void testBinomialCoefficientNumerosGrandes() {
        assertEquals(1287, StatUtils.binomialCoefficient(13, 3));
        assertEquals(3003, StatUtils.binomialCoefficient(15, 5));
        assertEquals(8008, StatUtils.binomialCoefficient(16, 6));
        assertEquals(48620, StatUtils.binomialCoefficient(20, 8));
        assertEquals(177100, StatUtils.binomialCoefficient(25, 10));
    }

    // ---------- Tests de Excepciones para binomialCoefficient ----------

    @Test
    @DisplayName("binomialCoefficient debe lanzar excepción cuando k < 0")
    void testBinomialCoefficientKNegativo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> StatUtils.binomialCoefficient(5, -1)
        );
        assertEquals("k must be in the range [0, n]", exception.getMessage());
    }

    @Test
    @DisplayName("binomialCoefficient debe lanzar excepción cuando k > n")
    void testBinomialCoefficientKMayorQueN() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> StatUtils.binomialCoefficient(5, 6)
        );
        assertEquals("k must be in the range [0, n]", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "0, -1",
            "0, 1",
            "5, -1",
            "5, 6",
            "10, -5",
            "10, 15"
    })
    @DisplayName("binomialCoefficient casos inválidos parametrizados")
    void testBinomialCoefficientCasosInvalidos(int n, int k) {
        assertThrows(IllegalArgumentException.class,
                () -> StatUtils.binomialCoefficient(n, k));
    }

    // ==================== TESTS PARA binomialDistribution ====================

    // ---------- Tests Básicos de binomialDistribution ----------

    @Test
    @DisplayName("binomialDistribution casos base con n=0")
    void testBinomialDistributionN0() {
        double[] result = StatUtils.binomialDistribution(0, 0.5);
        assertEquals(1, result.length);
        assertEquals(1.0, result[0], 0.0001);
    }

    @Test
    @DisplayName("binomialDistribution casos base con n=1")
    void testBinomialDistributionN1() {
        double[] result = StatUtils.binomialDistribution(1, 0.3);
        assertEquals(2, result.length);
        assertEquals(0.7, result[0], 0.0001); // P(X=0) = 0.7
        assertEquals(0.3, result[1], 0.0001); // P(X=1) = 0.3
    }

    @Test
    @DisplayName("binomialDistribution con p=0.5 y n=2")
    void testBinomialDistributionN2P05() {
        double[] result = StatUtils.binomialDistribution(2, 0.5);
        assertEquals(3, result.length);
        assertEquals(0.25, result[0], 0.0001); // P(X=0) = 0.25
        assertEquals(0.5, result[1], 0.0001);  // P(X=1) = 0.5
        assertEquals(0.25, result[2], 0.0001); // P(X=2) = 0.25
    }

    @Test
    @DisplayName("binomialDistribution con p=0.5 y n=3")
    void testBinomialDistributionN3P05() {
        double[] result = StatUtils.binomialDistribution(3, 0.5);
        assertEquals(4, result.length);
        assertEquals(0.125, result[0], 0.0001); // P(X=0)
        assertEquals(0.375, result[1], 0.0001); // P(X=1)
        assertEquals(0.375, result[2], 0.0001); // P(X=2)
        assertEquals(0.125, result[3], 0.0001); // P(X=3)
    }

    @Test
    @DisplayName("binomialDistribution suma de probabilidades debe ser 1")
    void testBinomialDistributionSumaProbabilidades() {
        int[] nValues = {1, 2, 3, 4, 5, 10, 15, 20};
        double[] pValues = {0.0, 0.1, 0.25, 0.5, 0.75, 0.9, 1.0};

        for (int n : nValues) {
            for (double p : pValues) {
                double[] result = StatUtils.binomialDistribution(n, p);
                double sum = Arrays.stream(result).sum();
                assertEquals(1.0, sum, 0.0001,
                        String.format("Suma para n=%d, p=%.2f debería ser 1.0", n, p));
            }
        }
    }

    @Test
    @DisplayName("binomialDistribution casos extremos p=0 y p=1")
    void testBinomialDistributionCasosExtremos() {
        // Caso p=0: solo P(X=0) = 1, resto = 0
        double[] resultP0 = StatUtils.binomialDistribution(5, 0.0);
        assertEquals(1.0, resultP0[0], 0.0001);
        for (int i = 1; i < resultP0.length; i++) {
            assertEquals(0.0, resultP0[i], 0.0001);
        }

        // Caso p=1: solo P(X=n) = 1, resto = 0
        double[] resultP1 = StatUtils.binomialDistribution(5, 1.0);
        assertEquals(1.0, resultP1[5], 0.0001);
        for (int i = 0; i < 5; i++) {
            assertEquals(0.0, resultP1[i], 0.0001);
        }
    }

    @Test
    @DisplayName("binomialDistribution tamaño del array debe ser n+1")
    void testBinomialDistributionTamañoArray() {
        for (int n = 0; n <= 20; n++) {
            double[] result = StatUtils.binomialDistribution(n, 0.5);
            assertEquals(n + 1, result.length,
                    String.format("Array para n=%d debería tener tamaño %d", n, n+1));
        }
    }

    // ---------- Tests de Excepciones para binomialDistribution ----------

    @Test
    @DisplayName("binomialDistribution debe lanzar excepción cuando n < 0")
    void testBinomialDistributionNNegativo() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> StatUtils.binomialDistribution(-1, 0.5)
        );
        assertEquals("n must be non-negative", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, -0.5, -1.0, 1.1, 1.5, 2.0})
    @DisplayName("binomialDistribution debe lanzar excepción para p fuera de [0,1]")
    void testBinomialDistributionPInvalido(double p) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> StatUtils.binomialDistribution(5, p)
        );
        assertEquals("p must be in the range [0,1]", exception.getMessage());
    }

    // ==================== TESTS PARA findSmallest ====================

    // ---------- Tests Básicos de findSmallest ----------

    @Test
    @DisplayName("findSmallest con lista de un elemento")
    void testFindSmallestUnElemento() {
        List<Integer> lista = Arrays.asList(42);
        assertEquals(0, StatUtils.findSmallest(lista));
    }

    @Test
    @DisplayName("findSmallest con lista de enteros")
    void testFindSmallestEnteros() {
        List<Integer> lista = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6);
        assertEquals(1, StatUtils.findSmallest(lista)); // Primer 1 está en índice 1
    }

    @Test
    @DisplayName("findSmallest con números negativos")
    void testFindSmallestNegativos() {
        List<Integer> lista = Arrays.asList(-1, -5, -3, -2);
        assertEquals(1, StatUtils.findSmallest(lista)); // -5 está en índice 1
    }

    @Test
    @DisplayName("findSmallest con números positivos y negativos")
    void testFindSmallestMixtos() {
        List<Integer> lista = Arrays.asList(5, -3, 2, -7, 1);
        assertEquals(3, StatUtils.findSmallest(lista)); // -7 está en índice 3
    }

    @Test
    @DisplayName("findSmallest con diferentes tipos de Number")
    void testFindSmallestDiferentesTipos() {
        List<Number> lista = Arrays.asList(3, 1.5, 4L, 0.5f, 2.0);
        assertEquals(3, StatUtils.findSmallest(lista)); // 0.5f está en índice 3
    }

    @Test
    @DisplayName("findSmallest con Double, Float, Long, Integer")
    void testFindSmallestTiposMixtos() {
        List<Number> lista = Arrays.asList(
                Integer.valueOf(10),
                Double.valueOf(2.5),
                Float.valueOf(1.5f),
                Long.valueOf(8L),
                Double.valueOf(0.1)
        );
        assertEquals(4, StatUtils.findSmallest(lista)); // 0.1 está en índice 4
    }

    @Test
    @DisplayName("findSmallest retorna primer índice en caso de empate")
    void testFindSmallestPrimerIndiceEmpate() {
        List<Integer> lista = Arrays.asList(3, 1, 4, 1, 5);
        assertEquals(1, StatUtils.findSmallest(lista)); // Primer 1 está en índice 1
    }

    @Test
    @DisplayName("findSmallest con todos los elementos iguales")
    void testFindSmallestTodosIguales() {
        List<Integer> lista = Arrays.asList(5, 5, 5, 5, 5);
        assertEquals(0, StatUtils.findSmallest(lista)); // Primer elemento
    }

    @Test
    @DisplayName("findSmallest con orden descendente")
    void testFindSmallestOrdenDescendente() {
        List<Integer> lista = Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1);
        assertEquals(8, StatUtils.findSmallest(lista)); // 1 está en índice 8
    }

    @Test
    @DisplayName("findSmallest con orden ascendente")
    void testFindSmallestOrdenAscendente() {
        List<Integer> lista = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(0, StatUtils.findSmallest(lista)); // 1 está en índice 0
    }

    // ---------- Tests de Excepciones para findSmallest ----------

    @Test
    @DisplayName("findSmallest debe lanzar excepción para lista null")
    void testFindSmallestListaNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> StatUtils.findSmallest(null)
        );
        assertEquals("List cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("findSmallest debe lanzar excepción para lista vacía")
    void testFindSmallestListaVacia() {
        List<Integer> listaVacia = new ArrayList<>();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> StatUtils.findSmallest(listaVacia)
        );
        assertEquals("List cannot be null or empty", exception.getMessage());
    }

    // ==================== TESTS PARA findBiggest ====================

    // ---------- Tests Básicos de findBiggest ----------

    @Test
    @DisplayName("findBiggest con lista de un elemento")
    void testFindBiggestUnElemento() {
        List<Integer> lista = Arrays.asList(42);
        assertEquals(0, StatUtils.findBiggest(lista));
    }

    @Test
    @DisplayName("findBiggest con lista de enteros")
    void testFindBiggestEnteros() {
        List<Integer> lista = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6);
        assertEquals(5, StatUtils.findBiggest(lista)); // 9 está en índice 5
    }

    @Test
    @DisplayName("findBiggest con números negativos")
    void testFindBiggestNegativos() {
        List<Integer> lista = Arrays.asList(-1, -5, -3, -2);
        assertEquals(0, StatUtils.findBiggest(lista)); // -1 está en índice 0
    }

    @Test
    @DisplayName("findBiggest con números positivos y negativos")
    void testFindBiggestMixtos() {
        List<Integer> lista = Arrays.asList(-5, 3, -2, 7, 1);
        assertEquals(3, StatUtils.findBiggest(lista)); // 7 está en índice 3
    }

    @Test
    @DisplayName("findBiggest retorna primer índice en caso de empate")
    void testFindBiggestPrimerIndiceEmpate() {
        List<Integer> lista = Arrays.asList(3, 9, 4, 9, 5);
        assertEquals(1, StatUtils.findBiggest(lista)); // Primer 9 está en índice 1
    }

    @Test
    @DisplayName("findBiggest con todos los elementos iguales")
    void testFindBiggestTodosIguales() {
        List<Integer> lista = Arrays.asList(5, 5, 5, 5, 5);
        assertEquals(0, StatUtils.findBiggest(lista)); // Primer elemento
    }

    @Test
    @DisplayName("findBiggest con orden ascendente")
    void testFindBiggestOrdenAscendente() {
        List<Integer> lista = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(8, StatUtils.findBiggest(lista)); // 9 está en índice 8
    }

    @Test
    @DisplayName("findBiggest con orden descendente")
    void testFindBiggestOrdenDescendente() {
        List<Integer> lista = Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1);
        assertEquals(0, StatUtils.findBiggest(lista)); // 9 está en índice 0
    }

    @Test
    @DisplayName("findBiggest con valores extremos")
    void testFindBiggestValoresExtremos() {
        List<Integer> lista = Arrays.asList(
                Integer.MIN_VALUE, 0, Integer.MAX_VALUE, -1000, 1000
        );
        assertEquals(2, StatUtils.findBiggest(lista)); // Integer.MAX_VALUE en índice 2
    }

    // ---------- Tests de Excepciones para findBiggest ----------

    @Test
    @DisplayName("findBiggest debe lanzar excepción para lista null")
    void testFindBiggestListaNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> StatUtils.findBiggest(null)
        );
        assertEquals("List cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("findBiggest debe lanzar excepción para lista vacía")
    void testFindBiggestListaVacia() {
        List<Integer> listaVacia = new ArrayList<>();
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> StatUtils.findBiggest(listaVacia)
        );
        assertEquals("List cannot be null or empty", exception.getMessage());
    }

    // ==================== TESTS PARA getAllIntInRange ====================

    // ---------- Tests Básicos de getAllIntInRange ----------

    @Test
    @DisplayName("getAllIntInRange con rango de un solo elemento")
    void testGetAllIntInRangeUnElemento() {
        List<Integer> result = StatUtils.getAllIntInRange(5, 5);
        assertEquals(1, result.size());
        assertEquals(Integer.valueOf(5), result.get(0));
    }

    @Test
    @DisplayName("getAllIntInRange con rango pequeño")
    void testGetAllIntInRangeRangoPequeño() {
        List<Integer> result = StatUtils.getAllIntInRange(1, 5);
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("getAllIntInRange con números negativos")
    void testGetAllIntInRangeNegativos() {
        List<Integer> result = StatUtils.getAllIntInRange(-3, -1);
        List<Integer> expected = Arrays.asList(-3, -2, -1);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("getAllIntInRange cruzando cero")
    void testGetAllIntInRangeCruzandoCero() {
        List<Integer> result = StatUtils.getAllIntInRange(-2, 2);
        List<Integer> expected = Arrays.asList(-2, -1, 0, 1, 2);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("getAllIntInRange con rango grande")
    void testGetAllIntInRangeRangoGrande() {
        List<Integer> result = StatUtils.getAllIntInRange(1, 100);
        assertEquals(100, result.size());
        assertEquals(Integer.valueOf(1), result.get(0));
        assertEquals(Integer.valueOf(100), result.get(99));

        // Verificar algunos elementos intermedios
        assertEquals(Integer.valueOf(50), result.get(49));
        assertEquals(Integer.valueOf(75), result.get(74));
    }

    @Test
    @DisplayName("getAllIntInRange con cero incluido")
    void testGetAllIntInRangeConCero() {
        List<Integer> result = StatUtils.getAllIntInRange(0, 3);
        List<Integer> expected = Arrays.asList(0, 1, 2, 3);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("getAllIntInRange verifica orden consecutivo")
    void testGetAllIntInRangeOrdenConsecutivo() {
        List<Integer> result = StatUtils.getAllIntInRange(10, 15);

        for (int i = 0; i < result.size() - 1; i++) {
            assertEquals(result.get(i) + 1, result.get(i + 1).intValue(),
                    "Los elementos deben ser consecutivos");
        }
    }

    @Test
    @DisplayName("getAllIntInRange tamaño correcto del rango")
    void testGetAllIntInRangeTamañoCorrecto() {
        // Rango [a, b] debe tener (b - a + 1) elementos
        int[][] rangos = {{1, 10}, {0, 5}, {-5, 5}, {-10, -1}, {100, 200}};

        for (int[] rango : rangos) {
            int start = rango[0];
            int end = rango[1];
            List<Integer> result = StatUtils.getAllIntInRange(start, end);
            int expectedSize = end - start + 1;
            assertEquals(expectedSize, result.size(),
                    String.format("Rango [%d, %d] debería tener %d elementos", start, end, expectedSize));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "1, 3, '1,2,3'",
            "0, 2, '0,1,2'",
            "-2, 0, '-2,-1,0'",
            "5, 7, '5,6,7'",
            "10, 10, '10'"
    })
    @DisplayName("getAllIntInRange casos parametrizados")
    void testGetAllIntInRangeParametrizados(int start, int end, String expectedStr) {
        List<Integer> result = StatUtils.getAllIntInRange(start, end);
        String[] expectedArray = expectedStr.split(",");
        List<Integer> expected = Arrays.stream(expectedArray)
                .map(Integer::parseInt)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        assertEquals(expected, result);
    }

    // ---------- Tests de Excepciones para getAllIntInRange ----------

    @Test
    @DisplayName("getAllIntInRange debe lanzar excepción cuando end < start")
    void testGetAllIntInRangeEndMenorQueStart() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> StatUtils.getAllIntInRange(5, 3)
        );
        assertEquals("Upper bound must be greater than or equal to lower bound", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 0",
            "5, 2",
            "0, -1",
            "10, 9",
            "100, 50"
    })
    @DisplayName("getAllIntInRange casos inválidos parametrizados")
    void testGetAllIntInRangeCasosInvalidos(int start, int end) {
        assertThrows(IllegalArgumentException.class,
                () -> StatUtils.getAllIntInRange(start, end));
    }

    // ==================== TESTS DE RENDIMIENTO Y EDGE CASES ====================

    @Test
    @DisplayName("Test de rendimiento con binomial grande")
    void testRendimientoBinomialGrande() {
        // Estos cálculos deberían ejecutarse en tiempo razonable
        long startTime = System.currentTimeMillis();

        StatUtils.binomialCoefficient(30, 15);
        StatUtils.binomialCoefficient(25, 12);
        StatUtils.binomialDistribution(20, 0.5);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Debería tomar menos de 1 segundo
        assertTrue(duration < 1000, "Los cálculos binomiales deberían ser rápidos");
    }

    @Test
    @DisplayName("Test de rendimiento con listas grandes")
    void testRendimientoListasGrandes() {
        // Crear lista grande
        List<Integer> listaGrande = StatUtils.getAllIntInRange(1, 10000);

        long startTime = System.currentTimeMillis();

        StatUtils.findSmallest(listaGrande.subList(0, 1000));
        StatUtils.findBiggest(listaGrande.subList(0, 1000));

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Debería ser muy rápido
        assertTrue(duration < 100, "Búsquedas en listas deberían ser muy rápidas");
    }

    @Test
    @DisplayName("Test de integridad con números en límites de Integer")
    void testIntegridadLimitesInteger() {
        List<Integer> listaExtremos = Arrays.asList(
                Integer.MIN_VALUE,
                Integer.MIN_VALUE + 1,
                -1, 0, 1,
                Integer.MAX_VALUE - 1,
                Integer.MAX_VALUE
        );

        assertEquals(0, StatUtils.findSmallest(listaExtremos)); // MIN_VALUE en índice 0
        assertEquals(6, StatUtils.findBiggest(listaExtremos));  // MAX_VALUE en índice 6
    }

    @Test
    @DisplayName("Test de inmutabilidad - métodos no modifican entrada")
    void testInmutabilidadMetodos() {
        List<Integer> original = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5));
        List<Integer> copia = new ArrayList<>(original);

        StatUtils.findSmallest(original);
        StatUtils.findBiggest(original);

        assertEquals(copia, original, "Las listas no deberían modificarse");
    }

    // ==================== TESTS ADICIONALES AVANZADOS ====================

    // ---------- Tests Matemáticos Avanzados para binomialCoefficient ----------

    @Test
    @DisplayName("binomialCoefficient verifica fórmula matemática directa")
    void testBinomialCoefficientFormulMatematica() {
        // Verificar usando la fórmula: C(n,k) = n! / (k! * (n-k)!)
        // Para números pequeños donde podemos calcular factorial
        for (int n = 0; n <= 10; n++) {
            for (int k = 0; k <= n; k++) {
                long nFactorial = factorial(n);
                long kFactorial = factorial(k);
                long nkFactorial = factorial(n - k);
                long expected = nFactorial / (kFactorial * nkFactorial);

                assertEquals(expected, StatUtils.binomialCoefficient(n, k),
                        String.format("C(%d,%d) usando fórmula factorial", n, k));
            }
        }
    }

    @Test
    @DisplayName("binomialCoefficient casos específicos del triángulo de Pascal")
    void testBinomialCoefficientTrianguloPascal() {
        // Verificar las primeras filas del triángulo de Pascal
        int[][] trianguloPascal = {
                {1},                    // fila 0
                {1, 1},                 // fila 1
                {1, 2, 1},              // fila 2
                {1, 3, 3, 1},           // fila 3
                {1, 4, 6, 4, 1},        // fila 4
                {1, 5, 10, 10, 5, 1},   // fila 5
                {1, 6, 15, 20, 15, 6, 1} // fila 6
        };

        for (int n = 0; n < trianguloPascal.length; n++) {
            for (int k = 0; k < trianguloPascal[n].length; k++) {
                assertEquals(trianguloPascal[n][k], StatUtils.binomialCoefficient(n, k),
                        String.format("Triángulo de Pascal: C(%d,%d)", n, k));
            }
        }
    }

    @Test
    @DisplayName("binomialCoefficient optimización con k > n/2")
    void testBinomialCoefficientOptimizacion() {
        // El método debería usar la optimización C(n,k) = C(n,n-k) cuando k > n/2
        // Verificamos que ambos dan el mismo resultado
        for (int n = 10; n <= 20; n++) {
            for (int k = n/2 + 1; k <= n; k++) {
                int result1 = StatUtils.binomialCoefficient(n, k);
                int result2 = StatUtils.binomialCoefficient(n, n - k);
                assertEquals(result1, result2,
                        String.format("Optimización: C(%d,%d) = C(%d,%d)", n, k, n, n-k));
            }
        }
    }

    // ---------- Tests Estadísticos Avanzados para binomialDistribution ----------

    @Test
    @DisplayName("binomialDistribution verifica media y varianza teóricas")
    void testBinomialDistributionMediaVarianza() {
        int n = 10;
        double p = 0.3;
        double[] dist = StatUtils.binomialDistribution(n, p);

        // Calcular media empírica
        double media = 0;
        for (int x = 0; x <= n; x++) {
            media += x * dist[x];
        }

        // Calcular varianza empírica
        double varianza = 0;
        for (int x = 0; x <= n; x++) {
            varianza += Math.pow(x - media, 2) * dist[x];
        }

        // Media teórica = n * p
        double mediaTeoria = n * p;
        // Varianza teórica = n * p * (1-p)
        double varianzaTeoria = n * p * (1 - p);

        assertEquals(mediaTeoria, media, 0.0001, "Media empírica vs teórica");
        assertEquals(varianzaTeoria, varianza, 0.0001, "Varianza empírica vs teórica");
    }

    @Test
    @DisplayName("binomialDistribution casos límite con probabilidades extremas")
    void testBinomialDistributionProbabilidadesExtremas() {
        int n = 20;

        // Caso p muy cercano a 0
        double[] distP001 = StatUtils.binomialDistribution(n, 0.01);
        assertTrue(distP001[0] > 0.8, "Con p=0.01, P(X=0) debería ser muy alto");
        assertTrue(distP001[n] < 0.0001, "Con p=0.01, P(X=n) debería ser muy bajo");

        // Caso p muy cercano a 1
        double[] distP099 = StatUtils.binomialDistribution(n, 0.99);
        assertTrue(distP099[0] < 0.0001, "Con p=0.99, P(X=0) debería ser muy bajo");
        assertTrue(distP099[n] > 0.8, "Con p=0.99, P(X=n) debería ser muy alto");
    }

    @Test
    @DisplayName("binomialDistribution simetría cuando p=0.5")
    void testBinomialDistributionSimetria() {
        int n = 10;
        double[] dist = StatUtils.binomialDistribution(n, 0.5);

        // Para p=0.5, la distribución debe ser simétrica: P(X=k) = P(X=n-k)
        for (int k = 0; k <= n/2; k++) {
            assertEquals(dist[k], dist[n-k], 0.0001,
                    String.format("Simetría: P(X=%d) = P(X=%d) cuando p=0.5", k, n-k));
        }
    }

    // ---------- Tests Avanzados para findSmallest ----------

    @Test
    @DisplayName("findSmallest con valores NaN y infinitos")
    void testFindSmallestValoresEspeciales() {
        List<Double> lista = Arrays.asList(1.0, Double.NaN, Double.NEGATIVE_INFINITY, 5.0);

        // Double.NEGATIVE_INFINITY debería ser el menor
        assertEquals(2, StatUtils.findSmallest(lista));
    }

    @Test
    @DisplayName("findSmallest consistencia con diferentes implementaciones de Number")
    void testFindSmallestConsistencia() {
        // Crear lista con diferentes implementaciones de Number que representan el mismo valor
        List<Number> lista = Arrays.asList(
                Integer.valueOf(5),
                Double.valueOf(5.0),
                Float.valueOf(5.0f),
                Long.valueOf(5L),
                Byte.valueOf((byte)3),  // Este debería ser el menor
                Short.valueOf((short)5)
        );

        assertEquals(4, StatUtils.findSmallest(lista)); // Byte(3) en índice 4
    }

    @Test
    @DisplayName("findSmallest con precisión decimal")
    void testFindSmallestPrecisionDecimal() {
        List<Double> lista = Arrays.asList(
                1.0000001,
                1.0000002,
                0.9999999,  // Este debería ser el menor
                1.0000000
        );

        assertEquals(2, StatUtils.findSmallest(lista));
    }

    // ---------- Tests de Robustez y Edge Cases ----------

    @Test
    @DisplayName("getAllIntInRange con rangos muy grandes")
    void testGetAllIntInRangeRangosGrandes() {
        // Test con rango grande pero manejable
        List<Integer> result = StatUtils.getAllIntInRange(1000, 1100);
        assertEquals(101, result.size());
        assertEquals(Integer.valueOf(1000), result.get(0));
        assertEquals(Integer.valueOf(1100), result.get(100));

        // Verificar que es una secuencia correcta
        for (int i = 0; i < result.size(); i++) {
            assertEquals(Integer.valueOf(1000 + i), result.get(i));
        }
    }

    @Test
    @DisplayName("Stress test con operaciones combinadas")
    void testStressCombinado() {
        // Test que combina múltiples operaciones
        for (int n = 1; n <= 15; n++) {
            // Generar rango
            List<Integer> rango = StatUtils.getAllIntInRange(1, n);

            // Encontrar extremos
            int indiceMenor = StatUtils.findSmallest(rango);
            int indiceMayor = StatUtils.findBiggest(rango);

            // Verificar consistencia
            assertEquals(0, indiceMenor, "El menor debería estar en índice 0");
            assertEquals(n-1, indiceMayor, "El mayor debería estar en índice n-1");

            // Calcular algunos coeficientes binomiales
            for (int k = 0; k <= Math.min(n, 10); k++) {
                int coef = StatUtils.binomialCoefficient(n, k);
                assertTrue(coef > 0, "Coeficiente binomial debe ser positivo");
            }
        }
    }

    @Test
    @DisplayName("Verificación de tipos de retorno y inmutabilidad")
    void testTiposRetornoInmutabilidad() {
        // Verificar que getAllIntInRange retorna lista mutable
        List<Integer> lista = StatUtils.getAllIntInRange(1, 5);
        assertTrue(lista instanceof ArrayList, "Debería retornar ArrayList");

        // Verificar que se puede modificar la lista retornada sin afectar futuras llamadas
        lista.set(0, 999);
        List<Integer> nuevaLista = StatUtils.getAllIntInRange(1, 5);
        assertEquals(Integer.valueOf(1), nuevaLista.get(0),
                "Nueva lista no debería verse afectada por modificaciones anteriores");
    }

    @Test
    @DisplayName("Verificación de overflow en binomialCoefficient")
    void testBinomialCoefficientOverflow() {
        // Verificar que no hay overflow en casos conocidos
        // C(20,10) = 184756
        assertEquals(184756, StatUtils.binomialCoefficient(20, 10));

        // C(25,12) = 5200300
        assertEquals(5200300, StatUtils.binomialCoefficient(25, 12));

        // Estos valores están dentro del rango de int
        assertTrue(StatUtils.binomialCoefficient(25, 12) > 0,
                "No debería haber overflow para valores razonables");
    }

    @Test
    @DisplayName("binomialDistribution invariantes matemáticas")
    void testBinomialDistributionInvariantes() {
        double[] pValues = {0.1, 0.25, 0.5, 0.75, 0.9};
        int[] nValues = {5, 10, 15, 20};

        for (int n : nValues) {
            for (double p : pValues) {
                double[] dist = StatUtils.binomialDistribution(n, p);

                // Invariante 1: Todas las probabilidades deben ser no-negativas
                for (double prob : dist) {
                    assertTrue(prob >= 0, "Todas las probabilidades deben ser ≥ 0");
                    assertTrue(prob <= 1, "Todas las probabilidades deben ser ≤ 1");
                }

                // Invariante 2: La probabilidad máxima debe estar cerca de la moda teórica
                int maxIndex = 0;
                for (int i = 1; i < dist.length; i++) {
                    if (dist[i] > dist[maxIndex]) {
                        maxIndex = i;
                    }
                }

                // La moda teórica está alrededor de n*p
                double modaTeorica = n * p;
                assertTrue(Math.abs(maxIndex - modaTeorica) <= 1,
                        String.format("Moda empírica (%d) cerca de teórica (%.2f)", maxIndex, modaTeorica));
            }
        }
    }

    // ---------- Tests de Comparación y Benchmarking ----------

    @Test
    @DisplayName("Comparación de rendimiento findSmallest vs findBiggest")
    void testComparacionRendimiento() {
        List<Integer> listaGrande = StatUtils.getAllIntInRange(1, 10000);
        Collections.shuffle(listaGrande); // Desordenar para test más realista

        long inicio1 = System.nanoTime();
        StatUtils.findSmallest(listaGrande);
        long tiempo1 = System.nanoTime() - inicio1;

        long inicio2 = System.nanoTime();
        StatUtils.findBiggest(listaGrande);
        long tiempo2 = System.nanoTime() - inicio2;

        // Ambos métodos deberían tener complejidad similar O(n)
        double ratio = (double) tiempo1 / tiempo2;
        assertTrue(ratio > 0.1 && ratio < 10,
                "Ambos métodos deberían tener rendimiento similar");
    }

    // Método auxiliar para calcular factorial
    private long factorial(int n) {
        if (n <= 1) return 1;
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}