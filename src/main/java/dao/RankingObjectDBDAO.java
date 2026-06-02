package dao;

import database.ObjectDBManager;
import model.Partida;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class RankingObjectDBDAO implements RankingDAO {

    @Override
    public void insertar(Partida p) {
        EntityManager em = ObjectDBManager.obtenerConexion();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Partida> obtenerTop5() {
        EntityManager em = ObjectDBManager.obtenerConexion();
        try {
            TypedQuery<Partida> q = em.createQuery(
                "SELECT p FROM Partida p ORDER BY p.racha DESC", Partida.class);
            q.setMaxResults(5);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public int obtenerMejorRacha(String nombre) {
        EntityManager em = ObjectDBManager.obtenerConexion();
        try {
            TypedQuery<Integer> q = em.createQuery(
                "SELECT MAX(p.racha) FROM Partida p WHERE p.nombre = ?1", Integer.class);
            q.setParameter(1, nombre);
            Integer r = q.getSingleResult();
            return r != null ? r : 0;
        } finally {
            em.close();
        }
    }
}
