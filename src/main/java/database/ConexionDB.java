package database;

import java.sql.*;

public class ConexionDB {
    private static String url = "jdbc:sqlite:ranking.db";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url);
    }

    public static void crearTabla() {
        try (Connection c = obtenerConexion();
             Statement st = c.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS ranking (" +
                       "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                       "nombre TEXT NOT NULL, racha INTEGER NOT NULL)");
        } catch (SQLException e) { System.err.println("Error BD: " + e.getMessage()); }
    }
}
