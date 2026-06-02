import dao.*;
import database.ConexionDB;
import model.Partida;
import org.junit.jupiter.api.*;
import service.RankingService;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BackendTest {

    private static RankingSQLiteDAO sqlite;
    private static RankingSerialDAO serial;

    @BeforeAll
    static void setup() {
        ConexionDB.crearTabla();
        sqlite = new RankingSQLiteDAO();
        serial = new RankingSerialDAO();
        try (var c = ConexionDB.obtenerConexion();
             var st = c.createStatement()) {
            st.execute("DELETE FROM ranking");
        } catch (Exception e) {}
    }

    @Test @Order(1)
    void testPartidaModelo() {
        Partida p = new Partida("Ana", 5);
        assertEquals("Ana", p.getNombre());
        assertEquals(5, p.getRacha());
    }

    @Test @Order(2)
    void testSQLiteInsertarYRecuperar() {
        sqlite.insertar(new Partida("Test1", 10));
        List<Partida> ranking = sqlite.obtenerTop5();
        assertTrue(ranking.stream().anyMatch(p -> p.getNombre().equals("Test1") && p.getRacha() == 10));
    }

    @Test @Order(3)
    void testSQLiteTop5Ordenado() {
        for (int i = 0; i < 6; i++)
            sqlite.insertar(new Partida("P" + i, i));
        List<Partida> top = sqlite.obtenerTop5();
        assertEquals(5, top.size());
        for (int i = 1; i < top.size(); i++)
            assertTrue(top.get(i-1).getRacha() >= top.get(i).getRacha());
    }

    @Test @Order(4)
    void testSerialInsertarYRecuperar() {
        serial.insertar(new Partida("Serial1", 7));
        List<Partida> ranking = serial.obtenerTop5();
        assertTrue(ranking.stream().anyMatch(p -> p.getNombre().equals("Serial1") && p.getRacha() == 7));
    }

    @Test @Order(5)
    void testService() {
        RankingService svc = new RankingService();
        svc.guardarRecord("Service1", 3);
        assertTrue(svc.obtenerRanking().getItems().stream().anyMatch(p -> p.getNombre().equals("Service1")));
    }
}
