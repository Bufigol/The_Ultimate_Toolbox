package com.the_ultimate_toolbox.models.core;

/**
 * Clase que representa un artículo en el sistema.
 * Extiende de AbstractModel para heredar funcionalidad base.
 *
 * @author The Ultimate Toolbox Team
 * @version 1.0
 * @since 1.0
 */
public class Articulo extends AbstractModel {
    private String titulo;
    private String enlace;
    private String descripcion;

    /**
     * Constructor por defecto.
     * Genera un ID único automáticamente.
     */
    public Articulo() {
        super();
    }

    /**
     * Constructor con todos los campos.
     *
     * @param titulo String con el título del artículo
     * @param enlace String con el enlace del artículo
     * @param descripcion String con la descripción del artículo
     */
    public Articulo(String titulo, String enlace, String descripcion) {
        super();
        setTitulo(titulo);
        setEnlace(enlace);
        setDescripcion(descripcion);
    }

    /**
     * Obtiene el título del artículo.
     *
     * @return String con el título
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del artículo.
     *
     * @param titulo String con el título
     * @throws IllegalArgumentException si el título es nulo o vacío
     */
    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede ser nulo o vacío");
        }
        this.titulo = titulo;
    }

    /**
     * Obtiene el enlace del artículo.
     *
     * @return String con el enlace
     */
    public String getEnlace() {
        return enlace;
    }

    /**
     * Establece el enlace del artículo.
     *
     * @param enlace String con el enlace
     * @throws IllegalArgumentException si el enlace es nulo o vacío
     */
    public void setEnlace(String enlace) {
        if (enlace == null || enlace.trim().isEmpty()) {
            throw new IllegalArgumentException("El enlace no puede ser nulo o vacío");
        }
        this.enlace = enlace;
    }

    /**
     * Obtiene la descripción del artículo.
     *
     * @return String con la descripción
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del artículo.
     *
     * @param descripcion String con la descripción
     * @throws IllegalArgumentException si la descripción es nula o vacía
     */
    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede ser nula o vacía");
        }
        this.descripcion = descripcion;
    }

    @Override
    public boolean isValid() {
        return super.isValid() &&
                titulo != null && !titulo.trim().isEmpty() &&
                enlace != null && !enlace.trim().isEmpty() &&
                descripcion != null && !descripcion.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Articulo{" +
                "id='" + getId() + '\'' +
                ", titulo='" + titulo + '\'' +
                ", enlace='" + enlace + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
