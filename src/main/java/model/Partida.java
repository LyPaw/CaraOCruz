package model;

import java.io.Serializable;

public class Partida implements Serializable {
    private final String nombre;
    private final int racha;

    public Partida(String nombre, int racha) {
        this.nombre = nombre;
        this.racha = racha;
    }

    public String getNombre() { return nombre; }
    public int getRacha() { return racha; }
}
