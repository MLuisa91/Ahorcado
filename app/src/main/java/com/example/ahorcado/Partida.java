package com.example.ahorcado;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Partida implements Serializable {

    private String palabra;
    private char[] palabraOculta;
    private int intentos;
    private List<String> introducidas = new ArrayList<>();

    public Partida(String palabra, char[] palabraOculta, int intentos, List introducidas){
        this.palabra = palabra;
        this.palabraOculta = palabraOculta;
        this.intentos = intentos;
        this.introducidas = introducidas;

    }

    public List<String> getIntroducidas() {
        return introducidas;
    }

    public void setIntroducidas(List<String> introducidas) {
        this.introducidas = introducidas;
    }

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public char[] getPalabraOculta() {
        return palabraOculta;
    }

    public void setPalabraOculta(char[] palabraOculta) {
        this.palabraOculta = palabraOculta;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }
}
