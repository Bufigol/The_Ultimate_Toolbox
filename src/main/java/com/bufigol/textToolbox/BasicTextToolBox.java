package com.bufigol.textToolbox;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean containsOnlyLetters(String test){ //see regex
        Pattern pattern = Pattern.compile("^[a-zA-ZñáéíóúÁÉÍÓÚ]+$");
        Matcher matcher = pattern.matcher(test);
        return matcher.matches();
    }
    public static double floatFromString(String str) {
        double out= 0.0;
        if (str == null) {
            return out;
        }
        try {
            out = Double.parseDouble(str);
            return out;
        } catch (NumberFormatException nfe) {
            return out;
        }
    }

    public static String removeCharacterFromString(String s, char c) {
        StringBuilder out= new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) {
                out.append(s.charAt(i));
            }
        }
        return out.toString();
    }
}
