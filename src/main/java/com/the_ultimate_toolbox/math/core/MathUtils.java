package com.ultimatetoolbox.math.core;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Provides general mathematical operations and utilities.
 * <p>
 * This class contains implementations of common mathematical operations such as:
 * <ul>
 *   <li>Rounding and multiples calculation</li>
 *   <li>Power and exponentiation</li>
 *   <li>Greatest common divisor</li>
 *   <li>Precision control for decimal numbers</li>
 * </ul>
 * </p>

 * @author Bufigol
 * @version 1.0
 * @since 1.0
 */
public class MathUtils {

    /**
     * Calculates the next multiple of ten nearest to a number.
     * <p>
     * This method rounds the input number to the nearest multiple of 10.
     * For example:
     * <ul>
     *   <li>12 rounds to 10</li>
     *   <li>15 rounds to 20</li>
     *   <li>25 rounds to 30</li>
     * </ul>
     * </p>
     *
     * @param number The number for which to find the nearest multiple of ten
     * @return The multiple of ten nearest to the input number
     */
    public static int nextMultipleOfTen(int number) {
        return (int) (Math.round((number + 5) / 10.0) * 10.0);
    }

    /**
     * Calculates the next multiple of hundred nearest to a number.
     * <p>
     * This method rounds the input number to the nearest multiple of 100.
     * For example:
     * <ul>
     *   <li>120 rounds to 100</li>
     *   <li>150 rounds to 200</li>
     *   <li>250 rounds to 300</li>
     * </ul>
     * </p>
     *
     * @param number The number for which to find the nearest multiple of hundred
     * @return The multiple of hundred nearest to the input number
     */
    public static int nextMultipleOfHundred(int number) {
        return (int) (Math.round((number + 50) / 100.0) * 100.0);
    }

    /**
     * Calculates the power of a number.
     * <p>
     * This method calculates base raised to the power of exponent.
     * The implementation uses an iterative approach for positive exponents
     * to avoid potential stack overflow with large exponents.
     * </p>
     *
     * @param base The base number
     * @param exponent The exponent (can be positive, negative, or zero)
     * @return base raised to the power of exponent
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
     * Calculates the greatest common divisor (GCD) of two integers using Euclid's algorithm.
     * <p>
     * The greatest common divisor is the largest positive integer that divides
     * both a and b without a remainder.
     * </p>
     * <p>
     * This implementation uses an iterative version of Euclid's algorithm.
     * </p>
     *
     * @param a First integer
     * @param b Second integer
     * @return The greatest common divisor of a and b
     *
     * @see <a href="https://en.wikipedia.org/wiki/Euclidean_algorithm">Euclidean Algorithm (Wikipedia)</a>
     */
    public static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }

    /**
     * Rounds a decimal number to a specified number of decimal places.
     * <p>
     * This method provides precise control over rounding decimal numbers.
     * It uses {@link RoundingMode#HALF_UP} which is the standard rounding mode
     * (round up if the digit to the right is ≥ 5, otherwise round down).
     * </p>
     *
     * @param value The number to round
     * @param places The number of decimal places to round to (must be non-negative)
     * @return The rounded value
     * @throws IllegalArgumentException If places is negative
     */
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException("The number of decimal places must be greater than or equal to zero");
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Rounds a number to its largest power of ten.
     * <p>
     * This method returns the largest power of ten that has the same
     * number of digits as the input. For example:
     * <ul>
     *   <li>5 returns 1 (10^0)</li>
     *   <li>42 returns 10 (10^1)</li>
     *   <li>789 returns 100 (10^2)</li>
     * </ul>
     * </p>
     *
     * @param n The number to round
     * @return The largest power of ten with the same number of digits as n
     */
    public static int roundToLastPowerOfTen(int n) {
        int length = String.valueOf(Math.abs(n)).length();
        String stTen = "1";

        for (int i = 0; i < length-1; i++) {
            stTen += "0";
        }

        return Integer.parseInt(stTen);
    }

    /**
     * Checks if two floating-point numbers are equal within a specified tolerance.
     * <p>
     * Due to the imprecise nature of floating-point arithmetic, exact equality
     * comparisons are often inappropriate. This method allows equality checking
     * within a specified epsilon value.
     * </p>
     *
     * @param a First number
     * @param b Second number
     * @param epsilon Tolerance for equality (must be positive)
     * @return true if the absolute difference between a and b is less than or equal to epsilon
     * @throws IllegalArgumentException If epsilon is negative
     */
    public static boolean equals(double a, double b, double epsilon) {
        if (epsilon < 0) {
            throw new IllegalArgumentException("Tolerance (epsilon) must be positive");
        }
        return Math.abs(a - b) <= epsilon;
    }

    /**
     * Constrains a value to be within a specified range.
     * <p>
     * If the value is less than min, min is returned.
     * If the value is greater than max, max is returned.
     * Otherwise, the original value is returned.
     * </p>
     *
     * @param value The value to constrain
     * @param min The lower bound of the range
     * @param max The upper bound of the range
     * @return The constrained value
     * @throws IllegalArgumentException If min is greater than max
     */
    public static double clamp(double value, double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("Minimum value must be less than or equal to maximum value");
        }
        return Math.max(min, Math.min(max, value));
    }
}