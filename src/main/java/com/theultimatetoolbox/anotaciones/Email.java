package com.theultimatetoolbox.anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

/**
 * Anotación para validar que un String representa una dirección de correo electrónico válida.
 * Utiliza una expresión regular estándar para la validación.
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE }) // Lugares donde se puede aplicar la anotación
@Retention(RetentionPolicy.RUNTIME) // La anotación estará disponible en tiempo de ejecución
@Documented // Incluir esta anotación en la documentación JavaDoc
public @interface Email {

    /**
     * @return El mensaje de error a mostrar si la validación falla.
     */
    String message() default "La dirección de correo electrónico no tiene un formato válido";

    // --- Elementos estándar de Bean Validation (opcionales pero recomendados) ---

    /**
     * @return Los grupos de validación a los que pertenece esta restricción.
     */
    Class<?>[] groups() default {};

    /**
     * @return La carga útil (payload) asociada con la restricción, utilizada por clientes avanzados.
     */
    Class<? extends java.lang.annotation.Annotation>[] payload() default {};

    // --- Parámetros específicos de la validación de Email (si quisiéramos personalización) ---

    /**
     * @return Expresión regular opcional para validar el formato del email.
     * Si no se especifica, se usará una expresión regular por defecto.
     */
    // String regexp() default ".*"; // Ejemplo si quisiéramos permitir regex custom

    /**
     * @return Banderas opcionales para la expresión regular (ej. Pattern.CASE_INSENSITIVE).
     */
    // java.util.regex.Pattern.Flag[] flags() default {}; // Ejemplo
} 