package service;

import dao.RankingDAO;
import dao.RankingSQLiteDAO;
import model.ListaResultados;
import model.Partida;

public class RankingService {
    private final RankingDAO dao = new RankingSQLiteDAO();

    public void guardarRecord(String nombre, int racha) {
        dao.insertar(new Partida(nombre, racha));
    }

    public void guardarSiMejor(String nombre, int racha) {
        if (racha > obtenerMejorRacha(nombre)) {
            dao.insertar(new Partida(nombre, racha));
        }
    }

    public int obtenerMejorRacha(String nombre) {
        return dao.obtenerMejorRacha(nombre);
    }

    public ListaResultados<Partida> obtenerRanking() {
        ListaResultados<Partida> lr = new ListaResultados<>();
        dao.obtenerTop5().forEach(lr::añadir);
        System.out.println("Suma total de rachas TOP5: " + lr.sumar(Partida::getRacha));
        return lr;
    }
}
