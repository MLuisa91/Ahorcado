package com.example.ahorcado;

public class Palabra {

    private final Integer id;
    private final String nombre;
    private final String descripcion;

    public Palabra(Integer id, String nombre, String descripcion){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
