package com.bufigol.modelage;

public class Articulo {
    private String titulo;
    private String enlace;
    private String descripcion;

    public Articulo(String titulo, String enlace, String descripcion) {
        this.titulo = titulo;
        this.enlace = enlace;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
