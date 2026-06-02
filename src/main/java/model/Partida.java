package model;

public class Partida {
    private Long id;
    private String nombre;
    private int racha;

    public Partida() {}

    public Partida(String nombre, int racha) {
        this.nombre = nombre;
        this.racha = racha;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getRacha() { return racha; }
    public void setRacha(int racha) { this.racha = racha; }
}
