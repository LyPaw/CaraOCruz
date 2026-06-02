package dao;

import database.ConexionDB;
import model.Partida;
import java.sql.*;
import java.util.*;

public class RankingSQLiteDAO implements RankingDAO {

    @Override
    public void insertar(Partida p) {
        String sql = "INSERT INTO ranking (nombre, racha) VALUES (?, ?)";
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement st = c.prepareStatement(sql)) {
            st.setString(1, p.getNombre());
            st.setInt(2, p.getRacha());
            st.executeUpdate();
        } catch (SQLException e) { System.err.println("Error insertar: " + e.getMessage()); }
    }

    @Override
    public List<Partida> obtenerTop5() {
        List<Partida> lista = new ArrayList<>();
        try (Connection c = ConexionDB.obtenerConexion();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT nombre, racha FROM ranking ORDER BY racha DESC LIMIT 5")) {
            while (rs.next()) lista.add(new Partida(rs.getString("nombre"), rs.getInt("racha")));
        } catch (SQLException e) { System.err.println("Error listar: " + e.getMessage()); }
        return lista;
    }

    @Override
    public int obtenerMejorRacha(String nombre) {
        String sql = "SELECT COALESCE(MAX(racha), 0) FROM ranking WHERE nombre = ?";
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement st = c.prepareStatement(sql)) {
            st.setString(1, nombre);
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.err.println("Error consulta: " + e.getMessage()); }
        return 0;
    }
}
