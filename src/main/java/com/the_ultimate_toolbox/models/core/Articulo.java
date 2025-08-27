package com.the_ultimate_toolbox.models.core;

/**
 * Represents an article with a title, link, and description.
 */
public class Articulo {
    private String titulo;
    private String enlace;
    private String descripcion;

    /**
     * Constructs a new Articulo object.
     *
     * @param titulo      the title of the article
     * @param enlace      the link to the article
     * @param descripcion the description of the article
     */
    public Articulo(String titulo, String enlace, String descripcion) {
        this.titulo = titulo;
        this.enlace = enlace;
        this.descripcion = descripcion;
    }

    /**
     * Gets the title of the article.
     *
     * @return the title of the article
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Sets the title of the article.
     *
     * @param titulo the new title of the article
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Gets the link to the article.
     *
     * @return the link to the article
     */
    public String getEnlace() {
        return enlace;
    }

    /**
     * Sets the link to the article.
     *
     * @param enlace the new link to the article
     */
    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    /**
     * Gets the description of the article.
     *
     * @return the description of the article
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Sets the description of the article.
     *
     * @param descripcion the new description of the article
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
