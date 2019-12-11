package com.example.bookkeep.Core;

import java.util.List;

public class Libro {

        public enum Genero {AVENTURAS, CIENCIA_FICCIÓN, DRAMA, FANTASIA, MISTERIO,
        POESIA, ROMANTICA, TEATRO, TERROR, OTRO};

        public Libro(String titulo, String autor, String imagen, Genero genero,
                     boolean leido, String reseña, double puntuacion) {
            this.titulo = titulo.trim();
            this.autor = autor.trim();
            this.imagen = imagen.trim();
            this.genero = genero;
            this.leido = leido;
            this.reseña = reseña.trim();
            this.puntuacion = puntuacion;
        }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public String getReseña() {
        return reseña;
    }

    public void setReseña(String reseña) {
        this.reseña = reseña;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    @Override
    public String toString() {
        return this.getTitulo() + ":" +
                this.getAutor() + ":" +
                this.getImagen() + ":" +
                this.getGenero() + ":" +
                this.getReseña() + ":" +
                this.getPuntuacion();
    }

        private String titulo;
        private String autor;
        private String imagen;
        private Genero genero;
        private boolean leido;
        private String reseña;
        private double puntuacion;


}
