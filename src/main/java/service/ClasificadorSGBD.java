package service;

public class ClasificadorSGBD {

    public static void analizar() {
        System.out.println("""
            \n=======================================================
            CLASIFICACIÓN DE SISTEMAS GESTORES DE BASES DE DATOS
            ========================================================
            """);
        mostrarSQLite();
    }

    private static void mostrarSQLite() {
        System.out.println("""
            --- SQLite (JDBC) ---
            Tipo:          Sistema Gestor de Bases de Datos Relacional (SGBDR)
            Método acceso: JDBC (Java Database Connectivity)
            API:           Statement, PreparedStatement, ResultSet
            Formato:       SQL (Structured Query Language)
            Archivo:       ranking.db
            Ventaja:       Lenguaje de consulta estandarizado (SQL).
                           Soporta transacciones ACID, joins, índices.
            Desventaja:    Esquema rígido (tablas definidas). Requiere mapeo
                           objeto-relacional manual (cada fila → objeto Partida).
            """);
    }
}
