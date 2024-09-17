package com.bufigol.input;

import java.util.Scanner;

public class Ingreso_Por_Teclado {

    public static String ingresarlinea(String msg) {
        try (Scanner teclado = new Scanner(System.in)) {
            System.out.print(msg);
            String out = teclado.nextLine();
            teclado.close();
            return out;
        } catch (Exception e) {
            System.out
                    .println("No ha ingresado una frase, ingrese la frase nuevamente: ");
            return ingresarlinea(msg);
        }
    }


    public static Long ingresarnumero() {
        return Long.parseLong(ingresarlinea("Ingresa un numero"));
    }


    public static Integer[] Ingresarvector(int longitud) {
        Scanner teclado = new Scanner(System.in);
        Integer[] vector = new Integer[longitud];
        for (int i = 0; i < vector.length; i++) {
            System.out.print("Ingrese el " + (i + 1) + " elemento: ");
            vector[i] = teclado.nextInt();
        }
        teclado.close();
        return vector;
    }
}
