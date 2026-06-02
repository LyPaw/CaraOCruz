package dao;

import model.Partida;
import java.util.List;

public interface RankingDAO {
    void insertar(Partida p);
    List<Partida> obtenerTop5();
    int obtenerMejorRacha(String nombre);
}
