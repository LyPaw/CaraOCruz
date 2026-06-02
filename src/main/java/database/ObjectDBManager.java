package database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ObjectDBManager {
    private static final String RUTA = "ranking.odb";
    private static EntityManagerFactory emf;

    public static EntityManager obtenerConexion() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(RUTA);
        }
        return emf.createEntityManager();
    }

    public static void cerrar() {
        if (emf != null) emf.close();
    }
}
