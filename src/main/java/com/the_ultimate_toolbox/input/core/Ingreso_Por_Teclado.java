package com.the_ultimate_toolbox.input.core;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Ingreso_Por_Teclado {

    private static final Scanner teclado = new Scanner(System.in);

    public static String ingresarlinea(String msg) {
        System.out.print(msg);
        while (true) {
            try {
                return teclado.nextLine();
            } catch (Exception e) {
                System.out.println("No ha ingresado una frase, ingrese la frase nuevamente: ");
                teclado.next(); // consume the invalid input
            }
        }
    }

    public static Long ingresarnumero(String msg) {
        System.out.print(msg);
        while (true) {
            try {
                return teclado.nextLong();
            } catch (InputMismatchException e) {
                System.out.println("No ha ingresado un número, ingrese el número nuevamente: ");
                teclado.next(); // consume the invalid input
            }
        }
    }

    public static Integer[] Ingresarvector(int longitud) {
        Integer[] vector = new Integer[longitud];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = ingresarnumero("Ingrese el " + (i + 1) + " elemento: ").intValue();
        }
        return vector;
    }

    public static void closeScanner() {
        teclado.close();
    }
}
