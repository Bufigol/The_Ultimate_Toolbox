package com.bufigol.textToolbox;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * This class, BasicTextToolBox, provides a set of utility methods for 
 * manipulating and testing strings. 
 */
public class BasicTextToolBox {

    /**
     * This method removes all special characters from a given string. 
     * It first normalizes the string using Normalizer.normalize() method 
     * and then removes all diacritical marks using a regular expression.
     * @param str The input string from which special characters need to be removed.
     * @return The output string with all special characters removed.
     */
    public static String removeEspecialCharacters(String str) {
        String out = "";
        String stringNormalizada = Normalizer.normalize(str, Normalizer.Form.NFD);
        out = stringNormalizada.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return out;
    }

    /**
     * This method checks if a given string is numeric or not. 
     * It tries to parse the string into a double and returns true if 
     * the parsing is successful, otherwise it returns false.
     * @param strNum The input string to be checked for numeric value.
     * @return True if the string is numeric, false otherwise.
     */
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

    /**
     * This method initializes a string array of a given size with empty strings.
     * @param size The size of the string array to be initialized.
     * @return The initialized string array.
     */
    public static String[] stringArrayInicialize(int size) {
        String[] arr = new String[size];
        for (int i = 0; i < size; i++) {
            arr[i] = "";
        }
        return arr;
    }

    /**
     * This method checks if a given string contains only letters or not. 
     * It uses a regular expression to match the string against only letters.
     * @param test The input string to be checked for only letters.
     * @return True if the string contains only letters, false otherwise.
     */
    public static boolean containsOnlyLetters(String test){ //see regex
        Pattern pattern = Pattern.compile("^[a-zA-ZñáéíóúÁÉÍÓÚ]+$");
        Matcher matcher = pattern.matcher(test);
        return matcher.matches();
    }

    /**
     * This method returns a float value from a given string. 
     * If the string is null or cannot be parsed into a float, it returns 0.0.
     * @param str The input string from which a float value needs to be extracted.
     * @return The float value extracted from the string.
     */
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

    /**
     * This method removes a given character from a string.
     * It uses a StringBuilder to build the output string by iterating 
     * through each character in the input string and appending it to the 
     * StringBuilder only if it's not the character to be removed.
     * @param s The input string from which a character needs to be removed.
     * @param c The character to be removed from the string.
     * @return The output string with the character removed.
     */
    public static String removeCharacterFromString(String s, char c) {
        StringBuilder out= new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) {
                out.append(s.charAt(i));
            }
        }
        return out.toString();
    }

    /**
     * This method checks if a given string is a palindrome or not. 
     * It creates a reversed string from the input string and checks 
     * if both strings are equal or not.
     * @param str The input string to be checked for palindrome.
     * @return True if the string is a palindrome, false otherwise.
     */
    public static boolean isPalindrome(String str) {
        String reversed = new StringBuilder(str).reverse().toString();
        return str.equals(reversed);
    }
}
