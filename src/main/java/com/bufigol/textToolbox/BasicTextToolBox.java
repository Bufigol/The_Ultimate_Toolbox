package com.bufigol.textToolbox;

import java.text.Normalizer;

public class BasicTextToolBox {
    public static String removeEspecialCharacters(String str) {
        String out = "";
        String stringNormalizada = Normalizer.normalize(str, Normalizer.Form.NFD);
        out = stringNormalizada.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return out;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    public static String[] stringArrayInicialize(int size) {
        String[] arr = new String[size];
        for (int i = 0; i < size; i++) {
            arr[i] = "";
        }
        return arr;
    }
}
