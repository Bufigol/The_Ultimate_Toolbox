package com.the_ultimate_toolbox.models.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Clase abstracta base que implementa la interfaz Model.
 * Proporciona implementaciones por defecto para métodos comunes.
 *
 * @author The Ultimate Toolbox Team
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractModel implements Model {
    
    private String id;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Constructor por defecto.
     */
    protected AbstractModel() {
        this.id = java.util.UUID.randomUUID().toString();
    }
    
    /**
     * Constructor con identificador específico.
     *
     * @param id String con el identificador único
     */
    protected AbstractModel(String id) {
        this.id = id;
    }
    
    @Override
    public String toJson() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir el modelo a JSON", e);
        }
    }
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public boolean isValid() {
        return id != null && !id.trim().isEmpty();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractModel that = (AbstractModel) o;
        return id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id='" + id + '\'' +
                '}';
    }
} 