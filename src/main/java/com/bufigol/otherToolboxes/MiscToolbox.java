package com.bufigol.otherToolboxes;

import java.util.Random;

public class MiscToolbox {

    public static int generateRandomNumber(int min, int max) {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        return rand.nextInt((max - min) + 1) + min;
    }

    public static int fibonacci(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }

}
