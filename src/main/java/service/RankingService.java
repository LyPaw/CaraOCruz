package service;

import dao.RankingDAO;
import dao.RankingSQLiteDAO;
import model.Partida;
import java.util.List;

public class RankingService {
    private final RankingDAO dao;

    public RankingService() {

        this(new RankingSQLiteDAO());
    }

    public RankingService(RankingDAO dao) {
        this.dao = dao;
        System.out.println("[RankingService] Usando: " + dao.getClass().getSimpleName());
    }

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

    public List<Partida> obtenerRanking() {
        return dao.obtenerTop5();
    }
}
