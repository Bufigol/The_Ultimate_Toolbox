package com.bufigol.comprobadores;

import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComprobadoresWeb {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String URL_REGEX =
            "^(https?://)?" + // protocolo
                    "((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|" + // nombre de dominio
                    "((\\d{1,3}\\.){3}\\d{1,3}))" + // O dirección IP (v4)
                    "(:\\d+)?(/[-a-z\\d%_.~+]*)*" + // puerto y path
                    "(\\?[;&a-z\\d%_.~+=-]*)?" + // query string
                    "(#[-a-z\\d_]*)?$"; // fragment locator

    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);


    /**
     * Valida si la cadena proporcionada es un email válido.
     *
     * La verificación se hace en dos pasos:
     * <ol>
     * <li>Se verifica que el email cumpla con el formato correcto (expresión regular).</li>
     * <li>Se verifica que el dominio del email tenga un registro DNS (se intenta obtener
     * el IP del dominio).</li>
     * </ol>
     *
     * @param email La cadena a validar
     * @return true si el email es válido, false en caso contrario
     */
    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            return false;
        }

        String domain = email.split("@")[1];
        try {
            InetAddress address = InetAddress.getByName(domain);
            return address.getHostAddress() != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida si la cadena proporcionada es una URL válida.
     *
     * @param url La cadena a validar
     * @return true si la URL es válida, false en caso contrario
     */
    public static boolean validarURL(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }

        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }
}
