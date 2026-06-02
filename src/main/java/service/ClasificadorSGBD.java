package service;

public class ClasificadorSGBD {

    public static void analizar() {
        System.out.println("""
            \n=======================================================
            CLASIFICACIÓN DE SISTEMAS GESTORES DE BASES DE DATOS
            =======================================================
            """);
        mostrarSQLite();
        mostrarObjectDB();
        mostrarSerializacion();
        System.out.println("""
            \n=======================================================
            ANÁLISIS COMPARATIVO
            ========================================================

            CRITERIO                | SQLite            | ObjectDB          | Serialización
            ------------------------|-------------------|-------------------|---------------------
            Tipo                    | Relacional        | Orientado a Objetos| Fichero binario
            Lenguaje de consulta    | SQL               | JPQL (JPA)        | N/A
            Almacenamiento          | ranking.db        | ranking.odb       | ranking_oo.dat
            Persistencia            | Tablas y filas    | Objetos directos  | Flujo de objetos
            Transacciones           | ACID (JDBC)       | ACID (JPA)        | No
            Consultas               | SELECT/WHERE      | JPQL/Criteria     | Streams en memoria
            Esquema                 | Fijo (tablas)     | Dinámico         | Dinámico
            Portabilidad            | Archivo único     | Archivo único     | Archivo único
            Acceso concurrente      | Sí (bloqueos)     | Sí (bloqueos)     | No
            Indexación              | Sí (SQL)          | Sí (JPA)          | No
            ========================================================
            """);
    }

    private static void mostrarSQLite() {
        System.out.println("""
            --- 1. SQLite (JDBC) ---
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

    private static void mostrarObjectDB() {
        System.out.println("""
            --- 2. ObjectDB (JPA) ---
            Tipo:          Sistema Gestor de Bases de Datos Orientado a Objetos (OODBMS)
            Método acceso: JPA (Java Persistence API) con EntityManager
            API:           EntityManager, TypedQuery, anotaciones @Entity
            Formato:       JPQL (Java Persistence Query Language) — similar a SQL
                           pero trabaja con objetos Java directamente.
            Archivo:       ranking.odb
            Ventaja:       Almacena objetos Java directamente sin mapeo.
                           Las anotaciones JPA (@Entity, @Id) definen la persistencia.
                           Soporta transacciones ACID y consultas con JPQL.
            Desventaja:    Requiere librería externa (ObjectDB). Menos conocido que SQL.
            """);
    }

    private static void mostrarSerializacion() {
        System.out.println("""
            --- 3. Serialización Java ---
            Tipo:          Persistencia basada en ficheros binarios (no es SGBD formal)
            Método acceso: Serialización/Deserialización con ObjectStreams
            API:           ObjectOutputStream, ObjectInputStream
            Formato:       Binario (formato propio de Java)
            Archivo:       ranking_oo.dat
            Ventaja:       Extremadamente simple. No requiere dependencias externas.
                           Almacena el grafo completo de objetos.
            Desventaja:    Sin lenguaje de consulta. Sin transacciones reales.
                           Sin concurrencia ni indexación. Carga todo en memoria.
            """);
    }
}
