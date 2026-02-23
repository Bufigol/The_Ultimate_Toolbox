package com.the_ultimate_toolbox.util.date;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

/**
 * Clase de utilidades para operaciones matemáticas con fechas.
 * Proporciona métodos para calcular diferencias entre fechas, sumar y restar días,
 * verificar años bisiestos, convertir entre zonas horarias y comparar fechas.
 * <p>
 * Soporta tanto la API moderna de Java 8+ (LocalDate, LocalDateTime) como
 * la API legacy (java.util.Date) para máxima compatibilidad.
 *
 * @author The Ultimate Toolbox
 * @version 1.1
 * @since 2025-01-09
 */
public class DateMath {

    /**
     * Calcula el número de días entre dos fechas.
     * El resultado es positivo si fechaFin es posterior a fechaInicio,
     * negativo si fechaFin es anterior a fechaInicio.
     *
     * @param fechaInicio La fecha inicial del cálculo
     * @param fechaFin La fecha final del cálculo
     * @return El número de días entre las dos fechas
     * @throws NullPointerException si alguna de las fechas es null
     * <p>
     * Ejemplo de uso:
     * <pre>
     * LocalDate inicio = LocalDate.of(2025, 1, 1);
     * LocalDate fin = LocalDate.of(2025, 1, 10);
     * long dias = DateMath.diasEntreFechas(inicio, fin); // Retorna 9
     * </pre>
     */
    public static long diasEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        Objects.requireNonNull(fechaInicio, "La fecha de inicio no puede ser null");
        Objects.requireNonNull(fechaFin, "La fecha de fin no puede ser null");

        return ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }

    /**
     * Calcula el número de días entre dos fechas usando java.util.Date.
     * El resultado es positivo si fechaFin es posterior a fechaInicio,
     * negativo si fechaFin es anterior a fechaInicio.
     * <p>
     * Este método convierte internamente las fechas Date a LocalDate usando
     * la zona horaria del sistema.
     *
     * @param fechaInicio La fecha inicial del cálculo (Date legacy)
     * @param fechaFin La fecha final del cálculo (Date legacy)
     * @return El número de días entre las dos fechas
     * @throws NullPointerException si alguna de las fechas es null
     * <p>
     * Ejemplo de uso:
     * <pre>
     * Date inicio = new Date(125, 0, 1); // 1 de enero de 2025
     * Date fin = new Date(125, 0, 10);   // 10 de enero de 2025
     * long dias = DateMath.diasEntreFechas(inicio, fin); // Retorna 9
     * </pre>
     * @since 1.1
     */
    public static long diasEntreFechas(Date fechaInicio, Date fechaFin) {
        Objects.requireNonNull(fechaInicio, "La fecha de inicio no puede ser null");
        Objects.requireNonNull(fechaFin, "La fecha de fin no puede ser null");

        LocalDate localDateInicio = fechaInicio.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate localDateFin = fechaFin.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return diasEntreFechas(localDateInicio, localDateFin);
    }

    /**
     * Suma una cantidad de días a una fecha dada.
     *
     * @param fecha La fecha base a la cual se le sumarán los días
     * @param diasASumar El número de días a sumar (puede ser negativo para restar)
     * @return Una nueva fecha con los días sumados
     * @throws NullPointerException si la fecha es null
     * <p>
     * Ejemplo de uso:
     * <pre>
     * LocalDate fecha = LocalDate.of(2025, 1, 15);
     * LocalDate nuevaFecha = DateMath.sumarDias(fecha, 10); // 2025-01-25
     * </pre>
     */
    public static LocalDate sumarDias(LocalDate fecha, long diasASumar) {
        Objects.requireNonNull(fecha, "La fecha no puede ser null");

        return fecha.plusDays(diasASumar);
    }

    /**
     * Suma una cantidad de días a una fecha dada usando java.util.Date.
     * <p>
     * Este método convierte internamente la fecha Date a LocalDate,
     * realiza la operación y devuelve el resultado como Date.
     *
     * @param fecha La fecha base a la cual se le sumarán los días (Date legacy)
     * @param diasASumar El número de días a sumar (puede ser negativo para restar)
     * @return Una nueva fecha Date con los días sumados
     * @throws NullPointerException si la fecha es null
     * <p>
     * Ejemplo de uso:
     * <pre>
     * Date fecha = new Date(125, 0, 15); // 15 de enero de 2025
     * Date nuevaFecha = DateMath.sumarDias(fecha, 10); // 25 de enero de 2025
     * </pre>
     * @since 1.1
     */
    public static Date sumarDias(Date fecha, long diasASumar) {
        Objects.requireNonNull(fecha, "La fecha no puede ser null");

        LocalDate localDate = fecha.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate resultado = sumarDias(localDate, diasASumar);

        return Date.from(resultado.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Resta una cantidad de días a una fecha dada.
     *
     * @param fecha La fecha base de la cual se restarán los días
     * @param diasARestar El número de días a restar (puede ser negativo para sumar)
     * @return Una nueva fecha con los días restados
     * @throws NullPointerException si la fecha es null
     * <p>
     * Ejemplo de uso:
     * <pre>
     * LocalDate fecha = LocalDate.of(2025, 1, 20);
     * LocalDate nuevaFecha = DateMath.restarDias(fecha, 5); // 2025-01-15
     * </pre>
     */
    public static LocalDate restarDias(LocalDate fecha, long diasARestar) {
        Objects.requireNonNull(fecha, "La fecha no puede ser null");

        return fecha.minusDays(diasARestar);
    }

    /**
     * Resta una cantidad de días a una fecha dada usando java.util.Date.
     * <p>
     * Este método convierte internamente la fecha Date a LocalDate,
     * realiza la operación y devuelve el resultado como Date.
     *
     * @param fecha La fecha base de la cual se restarán los días (Date legacy)
     * @param diasARestar El número de días a restar (puede ser negativo para sumar)
     * @return Una nueva fecha Date con los días restados
     * @throws NullPointerException si la fecha es null
     * <p>
     * Ejemplo de uso:
     * <pre>
     * Date fecha = new Date(125, 0, 20); // 20 de enero de 2025
     * Date nuevaFecha = DateMath.restarDias(fecha, 5); // 15 de enero de 2025
     * </pre>
     * @since 1.1
     */
    public static Date restarDias(Date fecha, long diasARestar) {
        Objects.requireNonNull(fecha, "La fecha no puede ser null");

        LocalDate localDate = fecha.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate resultado = restarDias(localDate, diasARestar);

        return Date.from(resultado.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Determina si un año específico es bisiesto.
     * Un año es bisiesto si:
     * - Es divisible por 4, Y
     * - NO es divisible por 100, A MENOS QUE
     * - También sea divisible por 400
     *
     * @param año El año a verificar
     * @return true si el año es bisiesto, false en caso contrario
     * <p>
     * Ejemplo de uso:
     * <pre>
     * boolean esBisiesto2024 = DateMath.esAñoBisiesto(2024); // true
     * boolean esBisiesto2025 = DateMath.esAñoBisiesto(2025); // false
     * boolean esBisiesto2000 = DateMath.esAñoBisiesto(2000); // true
     * boolean esBisiesto1900 = DateMath.esAñoBisiesto(1900); // false
     * </pre>
     */
    public static boolean esAñoBisiesto(int año) {
        return (año % 4 == 0 && año % 100 != 0) || (año % 400 == 0);
    }

    /**
     * Determina si el año de una fecha Date específica es bisiesto.
     * <p>
     * Este método extrae el año de la fecha proporcionada y verifica
     * si es bisiesto según las reglas estándar del calendario gregoriano.
     *
     * @param fecha La fecha de la cual extraer el año para verificar
     * @return true si el año de la fecha es bisiesto, false en caso contrario
     * @throws NullPointerException si la fecha es null
     * <p>
     * Ejemplo de uso:
     * <pre>
     * Date fecha2024 = new Date(124, 5, 15); // 15 de junio de 2024
     * Date fecha2025 = new Date(125, 5, 15); // 15 de junio de 2025
     * boolean esBisiesto2024 = DateMath.esAñoBisiesto(fecha2024); // true
     * boolean esBisiesto2025 = DateMath.esAñoBisiesto(fecha2025); // false
     * </pre>
     * @since 1.1
     */
    @SuppressWarnings("deprecation")
    public static boolean esAñoBisiesto(Date fecha) {
        Objects.requireNonNull(fecha, "La fecha no puede ser null");

        LocalDate localDate = fecha.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return esAñoBisiesto(localDate.getYear());
    }

    /**
     * Convierte una fecha y hora de una zona horaria a otra.
     *
     * @param fechaHora La fecha y hora a convertir
     * @param zonaOrigen La zona horaria de origen
     * @param zonaDestino La zona horaria de destino
     * @return La fecha y hora convertida a la zona horaria de destino
     * @throws NullPointerException si algún parámetro es null
     * <p>
     * Ejemplo de uso:
     * <pre>
     * LocalDateTime fechaHora = LocalDateTime.of(2025, 1, 15, 10, 30);
     * ZoneId zonaOrigen = ZoneId.of("America/New_York");
     * ZoneId zonaDestino = ZoneId.of("Europe/Madrid");
     * ZonedDateTime resultado = DateMath.convertirZonaHoraria(fechaHora, zonaOrigen, zonaDestino);
     * // Si en NY son las 10:30 AM, en Madrid serían las 4:30 PM
     * </pre>
     */
    public static ZonedDateTime convertirZonaHoraria(LocalDateTime fechaHora,
                                                      ZoneId zonaOrigen,
                                                      ZoneId zonaDestino) {
        Objects.requireNonNull(fechaHora, "La fecha y hora no puede ser null");
        Objects.requireNonNull(zonaOrigen, "La zona de origen no puede ser null");
        Objects.requireNonNull(zonaDestino, "La zona de destino no puede ser null");

        ZonedDateTime fechaEnZonaOrigen = fechaHora.atZone(zonaOrigen);
        return fechaEnZonaOrigen.withZoneSameInstant(zonaDestino);
    }

    /**
     * Convierte una fecha y hora de una zona horaria a otra usando java.util.Date.
     * <p>
     * Este método interpreta la fecha Date como perteneciente a la zona horaria
     * de origen y la convierte a la zona horaria de destino, devolviendo el
     * resultado como un nuevo Date.
     *
     * @param fechaHora La fecha y hora a convertir (Date legacy)
     * @param zonaOrigen La zona horaria de origen
     * @param zonaDestino La zona horaria de destino
     * @return Una nueva fecha Date en la zona horaria de destino
     * @throws NullPointerException si algún parámetro es null
     * <p>
     * Ejemplo de uso:
     * <pre>
     * Date fechaHora = new Date(125, 0, 15, 10, 30, 0); // 15 enero 2025, 10:30 AM
     * ZoneId zonaOrigen = ZoneId.of("America/New_York");
     * ZoneId zonaDestino = ZoneId.of("Europe/Madrid");
     * Date resultado = DateMath.convertirZonaHoraria(fechaHora, zonaOrigen, zonaDestino);
     * // Si en NY son las 10:30 AM, el Date resultante representa las 4:30 PM Madrid
     * </pre>
     * @since 1.1
     */
    public static Date convertirZonaHoraria(Date fechaHora,
                                           ZoneId zonaOrigen,
                                           ZoneId zonaDestino) {
        Objects.requireNonNull(fechaHora, "La fecha y hora no puede ser null");
        Objects.requireNonNull(zonaOrigen, "La zona de origen no puede ser null");
        Objects.requireNonNull(zonaDestino, "La zona de destino no puede ser null");

        // Convertir Date a LocalDateTime interpretándolo en la zona de origen
        Instant instant = fechaHora.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // Ahora usar el método existente para hacer la conversión
        ZonedDateTime resultado = convertirZonaHoraria(localDateTime, zonaOrigen, zonaDestino);

        // Convertir el resultado de vuelta a Date
        return Date.from(resultado.toInstant());
    }

    /**
     * Compara dos fechas y determina su relación temporal.
     *
     * @param fecha1 La primera fecha a comparar
     * @param fecha2 La segunda fecha a comparar
     * @param mismaZonaHoraria Si true, asume que ambas fechas están en la misma zona horaria.
     *                         Si false, convierte ambas a UTC antes de comparar.
     * @return Un valor entero:
     *         - Negativo si fecha1 es anterior a fecha2
     *         - Cero si fecha1 es igual a fecha2
     *         - Positivo si fecha1 es posterior a fecha2
     * @throws NullPointerException si alguna fecha es null
     * <p>
     * Ejemplo de uso:
     * <pre>
     * // Comparación simple (misma zona horaria)
     * LocalDateTime fecha1 = LocalDateTime.of(2025, 1, 15, 10, 0);
     * LocalDateTime fecha2 = LocalDateTime.of(2025, 1, 15, 14, 0);
     * int resultado = DateMath.compararFechas(fecha1, fecha2, true); // Negativo (fecha1 es anterior)
     *
     * // Comparación con diferentes zonas horarias
     * int resultado2 = DateMath.compararFechas(fecha1, fecha2, false); // Compara en UTC
     * </pre>
     */
    public static int compararFechas(LocalDateTime fecha1,
                                     LocalDateTime fecha2,
                                     boolean mismaZonaHoraria) {
        Objects.requireNonNull(fecha1, "La primera fecha no puede ser null");
        Objects.requireNonNull(fecha2, "La segunda fecha no puede ser null");

        if (mismaZonaHoraria) {
            return fecha1.compareTo(fecha2);
        } else {
            // Convertir ambas fechas a UTC para comparación neutral
            ZonedDateTime fecha1UTC = fecha1.atZone(ZoneId.systemDefault())
                                           .withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime fecha2UTC = fecha2.atZone(ZoneId.systemDefault())
                                           .withZoneSameInstant(ZoneId.of("UTC"));
            return fecha1UTC.compareTo(fecha2UTC);
        }
    }

    /**
     * Compara dos fechas usando java.util.Date y determina su relación temporal.
     * <p>
     * Este método convierte internamente las fechas Date a LocalDateTime para
     * realizar la comparación.
     *
     * @param fecha1 La primera fecha a comparar (Date legacy)
     * @param fecha2 La segunda fecha a comparar (Date legacy)
     * @param mismaZonaHoraria Si true, asume que ambas fechas están en la misma zona horaria.
     *                         Si false, convierte ambas a UTC antes de comparar.
     * @return Un valor entero:
     *         - Negativo si fecha1 es anterior a fecha2
     *         - Cero si fecha1 es igual a fecha2
     *         - Positivo si fecha1 es posterior a fecha2
     * @throws NullPointerException si alguna fecha es null
     * <p>
     * Ejemplo de uso:
     * <pre>
     * Date fecha1 = new Date(125, 0, 15, 10, 0, 0); // 15 enero 2025, 10:00 AM
     * Date fecha2 = new Date(125, 0, 15, 14, 0, 0); // 15 enero 2025, 2:00 PM
     * int resultado = DateMath.compararFechas(fecha1, fecha2, true); // Negativo
     * </pre>
     * @since 1.1
     */
    public static int compararFechas(Date fecha1,
                                    Date fecha2,
                                    boolean mismaZonaHoraria) {
        Objects.requireNonNull(fecha1, "La primera fecha no puede ser null");
        Objects.requireNonNull(fecha2, "La segunda fecha no puede ser null");

        if (mismaZonaHoraria) {
            return fecha1.compareTo(fecha2);
        } else {
            // Convertir Dates a LocalDateTime y usar el método existente
            LocalDateTime localDateTime1 = LocalDateTime.ofInstant(
                fecha1.toInstant(), ZoneId.systemDefault());
            LocalDateTime localDateTime2 = LocalDateTime.ofInstant(
                fecha2.toInstant(), ZoneId.systemDefault());

            return compararFechas(localDateTime1, localDateTime2, false);
        }
    }

    /**
     * Compara dos fechas con zonas horarias específicas.
     *
     * @param fecha1 La primera fecha a comparar
     * @param zona1 La zona horaria de la primera fecha
     * @param fecha2 La segunda fecha a comparar
     * @param zona2 La zona horaria de la segunda fecha
     * @return Un valor entero:
     *         - Negativo si fecha1 es anterior a fecha2
     *         - Cero si fecha1 es igual a fecha2
     *         - Positivo si fecha1 es posterior a fecha2
     * @throws NullPointerException si algún parámetro es null
     * <p>
     * Ejemplo de uso:
     * <pre>
     * LocalDateTime fechaNY = LocalDateTime.of(2025, 1, 15, 10, 0);
     * LocalDateTime fechaMadrid = LocalDateTime.of(2025, 1, 15, 16, 0);
     * ZoneId zonaNY = ZoneId.of("America/New_York");
     * ZoneId zonaMadrid = ZoneId.of("Europe/Madrid");
     * int resultado = DateMath.compararFechasConZonas(fechaNY, zonaNY, fechaMadrid, zonaMadrid);
     * // Compara los instantes reales considerando las zonas horarias
     * </pre>
     */
    public static int compararFechasConZonas(LocalDateTime fecha1, ZoneId zona1,
                                            LocalDateTime fecha2, ZoneId zona2) {
        Objects.requireNonNull(fecha1, "La primera fecha no puede ser null");
        Objects.requireNonNull(zona1, "La zona de la primera fecha no puede ser null");
        Objects.requireNonNull(fecha2, "La segunda fecha no puede ser null");
        Objects.requireNonNull(zona2, "La zona de la segunda fecha no puede ser null");

        ZonedDateTime zonedFecha1 = fecha1.atZone(zona1);
        ZonedDateTime zonedFecha2 = fecha2.atZone(zona2);

        // Comparar los instantes reales (convierte internamente a UTC)
        return zonedFecha1.toInstant().compareTo(zonedFecha2.toInstant());
    }

    /**
     * Compara dos fechas con zonas horarias específicas usando java.util.Date.
     * <p>
     * Este método interpreta cada fecha Date como perteneciente a su zona horaria
     * respectiva y compara los instantes reales en el tiempo.
     *
     * @param fecha1 La primera fecha a comparar (Date legacy)
     * @param zona1 La zona horaria de la primera fecha
     * @param fecha2 La segunda fecha a comparar (Date legacy)
     * @param zona2 La zona horaria de la segunda fecha
     * @return Un valor entero:
     *         - Negativo si fecha1 es anterior a fecha2
     *         - Cero si fecha1 es igual a fecha2
     *         - Positivo si fecha1 es posterior a fecha2
     * @throws NullPointerException si algún parámetro es null
     * <p>
     * Ejemplo de uso:
     * <pre>
     * Date fechaNY = new Date(125, 0, 15, 10, 0, 0); // 15 enero 2025, 10:00 AM
     * Date fechaMadrid = new Date(125, 0, 15, 16, 0, 0); // 15 enero 2025, 4:00 PM
     * ZoneId zonaNY = ZoneId.of("America/New_York");
     * ZoneId zonaMadrid = ZoneId.of("Europe/Madrid");
     * int resultado = DateMath.compararFechasConZonas(fechaNY, zonaNY, fechaMadrid, zonaMadrid);
     * // Compara considerando que son la misma hora en diferentes zonas
     * </pre>
     * @since 1.1
     */
    public static int compararFechasConZonas(Date fecha1, ZoneId zona1,
                                            Date fecha2, ZoneId zona2) {
        Objects.requireNonNull(fecha1, "La primera fecha no puede ser null");
        Objects.requireNonNull(zona1, "La zona de la primera fecha no puede ser null");
        Objects.requireNonNull(fecha2, "La segunda fecha no puede ser null");
        Objects.requireNonNull(zona2, "La zona de la segunda fecha no puede ser null");

        // Convertir Dates a LocalDateTime
        LocalDateTime localDateTime1 = LocalDateTime.ofInstant(
            fecha1.toInstant(), ZoneId.systemDefault());
        LocalDateTime localDateTime2 = LocalDateTime.ofInstant(
            fecha2.toInstant(), ZoneId.systemDefault());

        // Usar el método existente con LocalDateTime
        return compararFechasConZonas(localDateTime1, zona1, localDateTime2, zona2);
    }
}
