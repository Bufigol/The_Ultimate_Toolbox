package com.the_ultimate_toolbox.math.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides statistical utilities and data analysis functions.
 * <p>
 * This class contains implementations of common statistical operations such as:
 * <ul>
 *   <li>Binomial calculations (coefficient and distribution)</li>
 *   <li>Collection analysis (finding minimum and maximum values)</li>
 *   <li>Range generation and manipulation</li>
 * </ul>
 * </p>
 * <p>
 *
 * @author Bufigol
 * @version 1.0
 * @since 1.0
 */
public class StatUtils {

    /**
     * Calculates the binomial coefficient (n choose k).
     * <p>
     * The binomial coefficient is the number of ways to choose k elements from a set
     * of n elements without regard to order. This implementation uses an iterative
     * approach to avoid stack overflow for large inputs.
     * </p>
     *
     * @param n The total number of elements (must be non-negative)
     * @param k The number of elements to choose (must be between 0 and n, inclusive)
     * @return The binomial coefficient (n choose k)
     * @throws IllegalArgumentException If k is negative or greater than n
     *
     * @see <a href="https://en.wikipedia.org/wiki/Binomial_coefficient">Binomial Coefficient (Wikipedia)</a>
     */
    public static int binomialCoefficient(int n, int k) {
        if (k < 0 || k > n) {
            throw new IllegalArgumentException("k must be in the range [0, n]");
        }

        if (k == 0 || k == n) {
            return 1;
        }

        // Use the smaller value for optimization
        k = Math.min(k, n - k);

        int result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (n - (i - 1)) / i;
        }

        return result;
    }

    /**
     * Calculates the binomial probability distribution for n trials with probability p.
     * <p>
     * The binomial distribution is the discrete probability distribution of the number of
     * successes in a sequence of n independent experiments, each with a boolean-valued
     * outcome (success/failure) and probability p of success.
     * </p>
     * <p>
     * The returned array contains n+1 elements, where the element at index k represents
     * the probability of exactly k successes in n trials.
     * </p>
     *
     * @param n Number of trials (must be non-negative)
     * @param p Probability of success in each trial (must be between 0 and 1, inclusive)
     * @return An array containing the binomial probability distribution values
     * @throws IllegalArgumentException If n is negative or p is outside the range [0,1]
     *
     * @see <a href="https://en.wikipedia.org/wiki/Binomial_distribution">Binomial Distribution (Wikipedia)</a>
     */
    public static double[] binomialDistribution(int n, double p) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be non-negative");
        }
        if (p < 0 || p > 1) {
            throw new IllegalArgumentException("p must be in the range [0,1]");
        }

        double[] dist = new double[n+1];
        for (int x = 0; x <= n; x++) {
            dist[x] = binomialCoefficient(n, x) * Math.pow(p, x) * Math.pow(1-p, n-x);
        }
        return dist;
    }

    /**
     * Finds the index of the smallest number in a list of numbers.
     * <p>
     * This method compares all numbers in the list by converting them to double values.
     * In case of multiple equal minimum values, the index of the first occurrence is returned.
     * </p>
     *
     * @param list List of numbers to search (must not be null or empty)
     * @return The index of the smallest value in the list
     * @throws IllegalArgumentException If the list is null or empty
     */
    public static int findSmallest(List<? extends Number> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
        }

        int index = 0;
        for (int i = 1; i < list.size(); i++) {
            Double atI = list.get(i).doubleValue();
            Double atIndex = list.get(index).doubleValue();
            if (atI < atIndex) {
                index = i;
            }
        }
        return index;
    }

    /**
     * Finds the index of the largest integer in a list of integers.
     * <p>
     * In case of multiple equal maximum values, the index of the first occurrence is returned.
     * </p>
     *
     * @param list List of integers to search (must not be null or empty)
     * @return The index of the largest value in the list
     * @throws IllegalArgumentException If the list is null or empty
     */
    public static int findBiggest(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty");
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
     * Generates a list containing all integers in a specified range.
     * <p>
     * This method creates a list containing every integer from start to end, inclusive.
     * </p>
     *
     * @param start Lower bound of the range (inclusive)
     * @param end Upper bound of the range (inclusive)
     * @return A list containing all integers from start to end
     * @throws IllegalArgumentException If end is less than start
     */
    public static List<Integer> getAllIntInRange(int start, int end) {
        if (end < start) {
            throw new IllegalArgumentException("Upper bound must be greater than or equal to lower bound");
        }

        List<Integer> ints = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            ints.add(i);
        }
        return ints;
    }
}