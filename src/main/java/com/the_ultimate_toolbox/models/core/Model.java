package com.the_ultimate_toolbox.models.core;

import java.io.Serializable;

/**
 * Interfaz base para todos los modelos en The Ultimate Toolbox.
 * Define las operaciones básicas que todos los modelos deben implementar.
 *
 * @author The Ultimate Toolbox Team
 * @version 1.0
 * @since 1.0
 */
public interface Model extends Serializable {
    
    /**
     * Convierte el modelo a su representación en formato JSON.
     *
     * @return String con la representación JSON del modelo
     */
    String toJson();
    
    /**
     * Valida el estado actual del modelo.
     *
     * @return true si el modelo es válido, false en caso contrario
     */
    boolean isValid();
    
    /**
     * Obtiene un identificador único del modelo.
     *
     * @return String con el identificador único
     */
    String getId();
    
    /**
     * Establece el identificador único del modelo.
     *
     * @param id String con el identificador único
     */
    void setId(String id);
} 