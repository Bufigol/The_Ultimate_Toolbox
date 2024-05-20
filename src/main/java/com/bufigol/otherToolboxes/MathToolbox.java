package com.bufigol.otherToolboxes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * This class, MathToolbox, provides a collection of utility methods for mathematical operations.
 */
public class MathToolbox {

    /**
     * Returns the next multiple of ten that is greater than or equal to the given number.
     * This method rounds up the number to the nearest multiple of ten.
     *
     * @param number The number to find the next multiple of ten for.
     * @return The next multiple of ten.
     */
    public static int nextMultipleOfTen(int number) {
        int result = (int) (Math.round((number + 5) / 10.0) * 10.0);
        return result;
    }

    /**
     * Returns the next multiple of hundred that is greater than or equal to the given number.
     * This method rounds up the number to the nearest multiple of hundred.
     *
     * @param number The number to find the next multiple of hundred for.
     * @return The next multiple of hundred.
     */
    public static int nextMultipleOfHundred(int number) {
        int result = (int) (Math.round((number + 50) / 100.0) * 100.0);
        return result;
    }

    /**
     * Calculates the binomial coefficient (n choose k) using the recursive formula.
     *
     * @param n The number of items to choose from.
     * @param k The number of items to choose.
     * @return The binomial coefficient (n choose k).
     */
    public static int binomialCoefficient(int n, int k) {
        if (k == 0 || n == k) {
            return 1;
        } else {
            return binomialCoefficient(n - 1, k - 1) + binomialCoefficient(n - 1, k);
        }
    }

    /**
     * Calculates the probabilities of a binomial distribution with n trials and success probability p.
     *
     * @param n The number of trials.
     * @param p The success probability.
     * @return An array of probabilities for each number of successes from 0 to n.
     */
    public static double[] binomialDistribution(int n, double p) {
        double[] dist = new double[n + 1];
        for (int x = 0; x <= n; x++) {
            dist[x] = binomialCoefficient(n, x) * Math.pow(p, x) * Math.pow(1 - p, n - x);
        }
        return dist;
    }

    /**
     * Multiplies each element in the given array by a constant factor.
     *
     * @param array The array of numbers to multiply.
     * @param n     The constant factor.
     * @return A new array containing the multiplied numbers.
     */
    public static double[] multiplyArrayByDouble(double[] array, double n) {
        double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i] * n;
        }
        return result;
    }

    /**
     * Finds the index of the smallest number in the given list of numbers.
     *
     * @param list The list of numbers.
     * @return The index of the smallest number.
     */
    public static int findSmallest(List<? extends Number> list) {
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
     * Finds the index of the largest number in the given ArrayList of integers.
     *
     * @param arrayList The ArrayList of integers.
     * @return The index of the largest number.
     */
    public static int findBiggest(ArrayList<Integer> arrayList) {
        int index = 0;
        for (int i = 1; i < arrayList.size(); i++) {
            if (arrayList.get(i) > arrayList.get(index)) {
                index = i;
            }
        }
        return index;
    }

    /**
     * Returns a list of integers that contains every integer between start and end, inclusive.
     *
     * @param start The lower limit of the range, inclusive.
     * @param end   The upper limit of the range, inclusive.
     * @return A list of integers.
     */
    public static ArrayList<Integer> getAllIntInRange(int start, int end) {
        ArrayList<Integer> ints = new ArrayList<Integer>();
        for (int i = start; i <= end; i++) {
            ints.add(i);
        }
        return ints;
    }

    /**
     * Rounds a double to a certain number of decimals.
     *
     * @param value   The number that is going to be rounded.
     * @param places The number of decimals it will contain.
     * @return The rounded number.
     * @throws IllegalArgumentException If places is negative.
     */
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Returns the last power of ten that is less than or equal to the given number.
     *
     * @param n The number to find the last power of ten for.
     * @return The last power of ten.
     */
    public static int roundToLastPowerOfTen(int n) {
        int length = String.valueOf(n).length();
        String stTen = "1";

        for (int i = 0; i < length - 1; i++) {
            stTen += "0";
        }

        return Integer.valueOf(stTen);
    }

    /**
     * Calculates the power of a number using recursion.
     *
     * @param base   The base number.
     * @param
