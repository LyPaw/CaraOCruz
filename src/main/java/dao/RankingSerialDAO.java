package dao;

import model.Partida;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class RankingSerialDAO implements RankingDAO {
    private static final String FICHERO = "ranking_oo.dat";

    @Override
    public void insertar(Partida p) {
        List<Partida> lista = leerTodos();
        lista.add(p);
        escribirTodos(lista);
    }

    @Override
    public List<Partida> obtenerTop5() {
        return leerTodos().stream()
                .sorted(Comparator.comparingInt(Partida::getRacha).reversed())
                .limit(5).collect(Collectors.toList());
    }

    @Override
    public int obtenerMejorRacha(String nombre) {
        return leerTodos().stream()
                .filter(p -> p.getNombre().equals(nombre))
                .mapToInt(Partida::getRacha)
                .max().orElse(0);
    }

    @SuppressWarnings("unchecked")
    private List<Partida> leerTodos() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHERO))) {
            return (List<Partida>) ois.readObject();
        } catch (Exception e) { return new ArrayList<>(); }
    }

    private void escribirTodos(List<Partida> lista) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHERO))) {
            oos.writeObject(lista);
        } catch (Exception e) { System.err.println("Error guardando: " + e.getMessage()); }
    }
}
