package com.the_ultimate_toolbox.math.util;

import java.util.Random;

public class OtherMathUtils {
    /**
     * Generates and returns a random integer within the specified range (inclusive).
     * The random number generation is seeded with the current system time to ensure
     * different sequences of numbers on each call.
     *
     * @param min The minimum value of the random number range (inclusive).
     * @param max The maximum value of the random number range (inclusive).
     * @return A random integer within the specified range (inclusive).
     */
    public static int generateRandomNumber(int min, int max) {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis()); // Seed the random number generator with the current time
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Calculates and returns the nth number in the Fibonacci sequence.
     * The Fibonacci sequence is a series of numbers in which each number is the sum of the two preceding ones,
     * usually starting with 0 and 1.
     *
     * @param n The position of the desired Fibonacci number in the sequence.
     * @return The nth Fibonacci number.
     */
    public static int fibonacci(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }
}
