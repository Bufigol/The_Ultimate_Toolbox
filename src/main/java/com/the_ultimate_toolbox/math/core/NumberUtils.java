package com.the_ultimate_toolbox.math.core;

/**
 * Provides utilities for number manipulation and numeric operations.
 * <p>
 * This class contains implementations of common number operations such as:
 * <ul>
 *   <li>Factorial calculations</li>
 *   <li>Array manipulation operations</li>
 *   <li>Number transformations</li>
 * </ul>
 * </p>
 *
 * @author Bufigol
 * @version 1.0
 * @since 1.0
 */
public class NumberUtils {

    /**
     * Calculates the factorial of a non-negative integer.
     * <p>
     * The factorial of n (denoted as n!) is the product of all positive integers less than or equal to n.
     * By definition, 0! = 1.
     * </p>
     * <p>
     * This implementation uses an iterative approach instead of recursion to avoid stack overflow
     * for larger inputs.
     * </p>
     *
     * @param n The number for which to calculate the factorial (must be non-negative)
     * @return The factorial of n
     * @throws IllegalArgumentException If n is negative
     *
     * @see <a href="https://en.wikipedia.org/wiki/Factorial">Factorial (Wikipedia)</a>
     */
    public static int factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        }

        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * Multiplies each element of an array by a specified value.
     * <p>
     * This method creates a new array with the same length as the input array,
     * where each element is the product of the corresponding element in the input
     * array and the multiplier value.
     * </p>
     *
     * @param array The array whose elements are to be multiplied (can be null)
     * @param n The value by which to multiply each element
     * @return A new array with each element multiplied by n, or null if the input array is null
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