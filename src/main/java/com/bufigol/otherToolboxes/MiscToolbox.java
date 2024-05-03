package com.bufigol.otherToolboxes;

import java.util.Random;

public class MiscToolbox {

    public static int generateRandomNumber(int min, int max) {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        return rand.nextInt((max - min) + 1) + min;
    }

}
