# Documentación completa — Cara o Cruz

## Líneas de código por archivo

| Archivo | Líneas |
|---------|-------:|
| `src/main/java/app/Main.java` | 103 |
| `src/main/java/controller/JuegoController.java` | 91 |
| `src/main/java/controller/MusicManager.java` | 23 |
| `src/main/java/dao/RankingDAO.java` | 10 |
| `src/main/java/dao/RankingSQLiteDAO.java` | 41 |
| `src/main/java/dao/RankingSerialDAO.java` | 45 |
| `src/main/java/database/ConexionDB.java` | 20 |
| `src/main/java/model/ListaResultados.java` | 16 |
| `src/main/java/model/Partida.java` | 16 |
| `src/main/java/service/RankingService.java` | 31 |
| `src/main/resources/css/estilo.css` | 9 |
| `src/test/java/BackendTest.java` | 63 |
| `pom.xml` | 143 |
| `README.md` | 86 |
| **Total** | **1869** |

## Índice

1. [Estructura del proyecto](#estructura-del-proyecto)
2. [pom.xml — Configuración Maven](#pomxml--configuración-maven)
3. [app.Main — Punto de entrada](#appmain--punto-de-entrada)
4. [controller.JuegoController — Lógica del juego](#controllerjuegocontroller--lógica-del-juego)
5. [controller.MusicManager — Música de fondo](#controllermusicmanager--música-de-fondo)
6. [model.Partida — Modelo de datos](#modelpartida--modelo-de-datos)
7. [model.ListaResultados — Contenedor genérico](#modellistaresultados--contenedor-genérico)
8. [dao.RankingDAO — Interfaz de persistencia](#daorankingdao--interfaz-de-persistencia)
9. [dao.RankingSQLiteDAO — Implementación SQLite](#daorankingsqlitedao--implementación-sqlite)
10. [dao.RankingSerialDAO — Implementación serialización](#daorankingserialdao--implementación-serialización)
11. [database.ConexionDB — Conexión a base de datos](#databaseconexiondb--conexión-a-base-de-datos)
12. [service.RankingService — Capa de negocio](#servicerankingservice--capa-de-negocio)
13. [css/estilo.css — Estilos visuales](#cssestilocss--estilos-visuales)
14. [BackendTest — Tests unitarios](#backendtest--tests-unitarios)
15. [Flujo completo de ejecución](#flujo-completo-de-ejecución)
16. [Diagrama de dependencias](#diagrama-de-dependencias)

---

## Estructura del proyecto

```
src/
├── main/
│   ├── java/
│   │   ├── app/
│   │   │   └── Main.java                    ← Punto de entrada JavaFX
│   │   ├── controller/
│   │   │   ├── JuegoController.java          ← Controlador del juego
│   │   │   └── MusicManager.java             ← Gestor de música
│   │   ├── dao/
│   │   │   ├── RankingDAO.java               ← Interfaz DAO
│   │   │   ├── RankingSerialDAO.java         ← DAO con serialización
│   │   │   └── RankingSQLiteDAO.java         ← DAO con SQLite
│   │   ├── database/
│   │   │   └── ConexionDB.java               ← Conexión JDBC
│   │   ├── model/
│   │   │   ├── ListaResultados.java           ← Colección genérica
│   │   │   └── Partida.java                   ← Modelo de datos
│   │   └── service/
│   │       └── RankingService.java            ← Lógica de negocio
│   └── resources/
│       ├── css/
│       │   └── estilo.css                     ← Estilos JavaFX
│       ├── img/
│       │   ├── cara.png                       ← Imagen CARA
│       │   └── cruz.png                       ← Imagen CRUZ
│       └── sound/
│           └── musica_fondo.wav               ← Música de fondo
└── test/
    └── java/
        └── BackendTest.java                   ← Tests JUnit
```

---

## pom.xml — Configuración Maven

**Rol:** Define el proyecto, sus dependencias y los plugins de compilación/ejecución.

### Elementos principales

```xml
<groupId>app</groupId>
<artifactId>caraocruz</artifactId>
<version>1.0.0</version>
```

Identificadores únicos del proyecto. `app.caraocruz` es el nombre del artefacto.

```xml
<maven.compiler.release>17</maven.compiler.release>
<javafx.version>21</javafx.version>
```

Java 17 es la versión mínima de compilación. JavaFX 21 es la versión de la biblioteca gráfica.

### Dependencias

| Dependencia | Versión | Propósito |
|-------------|---------|-----------|
| `javafx-controls` | 21 | Botones, layouts, escenas, efectos visuales |
| `javafx-graphics` | 21 | Renderizado gráfico (con clasificador de plataforma) |
| `javafx-media` | 21 | Reproducción de audio (WAV) |
| `sqlite-jdbc` | 3.45.3.0 | Driver JDBC para conectar con SQLite |
| `junit-jupiter` | 5.11.0 | Tests unitarios (solo en test) |

### Plugins de build

| Plugin | Función |
|--------|---------|
| `maven-compiler-plugin` | Compila el código fuente a Java 17 |
| `maven-dependency-plugin` | Copia todas las dependencias (incluyendo nativas de JavaFX) a `target/lib/` |
| `javafx-maven-plugin` | Permite ejecutar con `mvn javafx:run` |
| `exec-maven-plugin` | Ejecuta la app directamente con `java --module-path ...` |

### Perfiles de plataforma

```xml
<profile id="windows"><javafx.platform>win</javafx.platform></profile>
<profile id="linux">  <javafx.platform>linux</javafx.platform></profile>
<profile id="mac">    <javafx.platform>mac</javafx.platform></profile>
```

Cada SO necesita su propia implementación nativa de JavaFX. Maven selecciona automáticamente el perfil según el sistema operativo.

---

## app.Main — Punto de entrada

**Rol:** Clase principal que extiende `javafx.application.Application`. Configura toda la interfaz gráfica y arranca el juego.

### Método `main(String[] args)`

```java
public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.print("=== CARA O CRUZ ===\nNombre: ");
    String n = sc.nextLine().trim();
    if (n.isEmpty()) n = "Jugador";
    System.setProperty("player.name", n);
    launch(args);
}
```

**Propósito:** Es el punto de entrada de la JVM. Pide el nombre del jugador por consola y lo guarda como una propiedad del sistema (`player.name`) para que esté disponible en toda la aplicación. Si el usuario no introduce nada, se asigna "Jugador" por defecto. Finalmente llama a `launch(args)` que inicia el ciclo de vida JavaFX.

**Flujo:**
1. Crea un `Scanner` para leer desde `System.in` (entrada estándar del terminal)
2. Muestra el mensaje `=== CARA O CRUZ ===\nNombre: `
3. Lee una línea de texto y elimina espacios al inicio/final con `trim()`
4. Si la cadena está vacía, asigna "Jugador"
5. Guarda el nombre en `System.setProperty("player.name", n)` — esto lo deja disponible globalmente
6. Llama a `launch(args)` que JavaFX interpreta para iniciar la ventana gráfica llamando al método `start()`

### Método `start(Stage v)`

```java
@Override
public void start(Stage v) {
```

**Propósito:** Es el método principal de JavaFX. Se ejecuta automáticamente después de `launch()`. Recibe el `Stage` (ventana) principal del sistema. Aquí se construye toda la interfaz.

#### Inicializaciones

```java
ConexionDB.crearTabla();
MusicManager.iniciar();
```

- `ConexionDB.crearTabla()` → Crea la tabla `ranking` en SQLite si no existe (ver sección ConexionDB).
- `MusicManager.iniciar()` → Carga y reproduce el archivo WAV de música de fondo en bucle infinito.

#### Gradiente de la moneda

```java
RadialGradient oro = new RadialGradient(
    0, 0.5,      // ángulo de enfoque (0 = circular uniforme)
    0.5, 0.5,    // centro del gradiente (50%, 50%)
    0.5,         // radio (50% del círculo)
    true,        // proporcional (se adapta al tamaño)
    CycleMethod.NO_CYCLE,   // no se repite
    new Stop(0, Color.web("#8D8877")),     // centro: gris oliva claro
    new Stop(0.3, Color.web("#676452")),   // interior: marrón medio
    new Stop(0.7, Color.web("#474639")),   // exterior: marrón oscuro
    new Stop(1, Color.web("#212118"))      // borde: casi negro
);
```

**Significado:** Crea un gradiente radial (de dentro hacia fuera) que simula el brillo metálico de una moneda. Los colores están extraídos de las imágenes reales `cara.png` y `cruz.png` para que el fondo combine visualmente con los dibujos de la moneda.

Los 8 parámetros del constructor `RadialGradient`:
1. `0` → ángulo de enfoque (0 = sin dirección preferida)
2. `0.5` → distancia del foco (centro)
3. `0.5, 0.5` → coordenadas del centro del gradiente
4. `0.5` → radio del gradiente
5. `true` → el radio es proporcional al tamaño del círculo
6. `CycleMethod.NO_CYCLE` → el gradiente no se repite fuera del círculo
7. `Stop` → puntos de color con su posición (0 = centro, 1 = borde)

#### Círculo de la moneda

```java
Circle circulo = new Circle(160, oro);
circulo.setStroke(Color.web("#1e2014"));   // borde marrón muy oscuro
circulo.setStrokeWidth(5);                  // grosor 5 píxeles
circulo.setEffect(new DropShadow(25, 7, 7, Color.rgb(30, 30, 25, 0.6)));
```

**Significado:** Crea un círculo de radio 160 píxels pintado con el gradiente `oro`. Le añade un borde fino y una sombra paralela (`DropShadow`) con desplazamiento 7px a la derecha y abajo, desenfoque 25px, y color negro semitransparente. Esto da un efecto 3D de moneda flotando sobre el fondo.

Parámetros de `DropShadow`:
1. `25` → radio de desenfoque (blur)
2. `7` → desplazamiento en X
3. `7` → desplazamiento en Y
4. `Color.rgb(30, 30, 25, 0.6)` → color de la sombra (negro con 60% opacidad)

#### Imágenes de las caras

```java
Image imgCara = new Image(getClass().getResourceAsStream("/img/cara.png"));
Image imgCruz = new Image(getClass().getResourceAsStream("/img/cruz.png"));
ImageView estrella = new ImageView(imgCara);
ImageView cruzFig = new ImageView(imgCruz);
estrella.setFitWidth(280); estrella.setPreserveRatio(true);
cruzFig.setFitWidth(280);  cruzFig.setPreserveRatio(true);
cruzFig.setVisible(false);
```

**Significado:** Carga las imágenes PNG de las dos caras de la moneda desde los recursos del proyecto. Las envuelve en `ImageView` (nodos JavaFX que muestran imágenes). Ambas se redimensionan a 280px de ancho manteniendo la relación de aspecto. La cara "CRUZ" comienza oculta (`setVisible(false)`) porque la moneda siempre empieza mostrando CARA.

`getClass().getResourceAsStream(...)` busca el archivo dentro del classpath (dentro de `target/classes/` tras compilar).

#### StackPane de la moneda

```java
StackPane moneda = new StackPane(circulo, estrella, cruzFig);
```

**Significado:** `StackPane` apila todos sus hijos uno encima de otro centrados. El orden importa: primero el círculo (fondo metálico), luego `estrella` (imagen CARA), luego `cruzFig` (imagen CRUZ). Como `cruzFig` empieza invisible, solo se ve el círculo con la imagen de CARA encima.

#### Botones

```java
Button cara = new Button("CARA");
cara.setStyle("-fx-base:#2E7D32;-fx-font-size:14;-fx-padding:10 28;");   // verde oscuro

Button verR = new Button("VER RANKING");
verR.setStyle("-fx-base:#1565C0;-fx-font-size:13;-fx-padding:10 16;");    // azul

Button cruz = new Button("CRUZ");
cruz.setStyle("-fx-base:#C62828;-fx-font-size:14;-fx-padding:10 28;");    // rojo oscuro

for (Button b : new Button[]{cara, verR, cruz})
    b.getStyleClass().add("boton-juego");
```

**Significado:** Tres botones con estilos inline y una clase CSS común. Cada botón tiene un color base diferente:
- **CARA** → verde (`#2E7D32`) para asociación psicológica con "acertar"
- **CRUZ** → rojo (`#C62828`) para asociación con "fallar"
- **VER RANKING** → azul (`#1565C0`) neutro

La clase `boton-juego` (definida en `estilo.css`) añade texto blanco, negrita, bordes redondeados y una sombra.

#### ListView del ranking

```java
ListView<String> ranking = new ListView<>();
ranking.setPrefHeight(200);
ranking.setStyle("-fx-control-inner-background:#0D1B3E;-fx-text-fill:white;" +
        "-fx-font-size:13;-fx-font-family:Consolas;-fx-background-radius:8;");
```

**Significado:** Lista vertical que muestra las puntuaciones del ranking. Estilizada con fondo azul marino oscuro, texto blanco en fuente monoespaciada Consolas y bordes redondeados.

#### Obtención del nombre y creación del controlador

```java
String nombre = System.getProperty("player.name", "Jugador");
System.out.printf("Bienvenido %s! Consigue la racha mas larga posible!%n", nombre);
```

Recupera el nombre guardado en `System.setProperty` desde `main()`.

```java
JuegoController ctrl = new JuegoController(moneda, estrella, cruzFig, ranking, cara, cruz, verR, nombre);
```

Crea el controlador pasándole todos los nodos visuales que necesita manipular.

#### Asignación de eventos

```java
cara.setOnAction(e -> ctrl.jugar("CARA"));
cruz.setOnAction(e -> ctrl.jugar("CRUZ"));
verR.setOnAction(e -> ctrl.mostrarRanking());
```

**Significado:** Asigna una acción a cada botón usando expresiones lambda. Cuando el usuario pulsa un botón:
- CARA → llama a `ctrl.jugar("CARA")`
- CRUZ → llama a `ctrl.jugar("CRUZ")`
- VER RANKING → llama a `ctrl.mostrarRanking()` que muestra el top 5

#### Layout principal

```java
HBox filaBotones = new HBox(12, cara, verR, cruz);
filaBotones.setAlignment(Pos.CENTER);

VBox raiz = new VBox(22, moneda, filaBotones, ranking);
raiz.setAlignment(Pos.CENTER);
raiz.setStyle("-fx-padding:40;-fx-background-color:linear-gradient(to bottom,#1A237E,#283593);");
```

**Significado:** El layout es un `VBox` (caja vertical) que contiene de arriba a abajo:
1. La moneda (StackPane)
2. La fila de botones (HBox)
3. El ranking (ListView)

El `VBox` tiene un espaciado de 22 píxels entre hijos y padding de 40 píxels alrededor. El fondo es un degradado lineal vertical de azul oscuro (`#1A237E`) a azul medio (`#283593`).

El `HBox` de botones tiene espaciado de 12 píxels entre botones.

#### Escena y ventana

```java
Scene escena = new Scene(raiz, 500, 600);
escena.getStylesheets().add(getClass().getResource("/css/estilo.css").toExternalForm());
```

Crea la escena con el layout raíz, tamaño 500x600, y carga la hoja de estilos CSS.

```java
v.setScene(escena);
v.setFullScreen(true);
v.setTitle("Cara o Cruz");
v.getIcons().add(imgCara);
v.show();
```

**Significado:** Asigna la escena al Stage, lo pone en pantalla completa, establece el título, añade la imagen CARA como icono de la ventana (aparece en la barra de tareas), y finalmente muestra la ventana.

---

## controller.JuegoController — Lógica del juego

**Rol:** Controlador principal del juego. Gestiona las animaciones, la lógica de acierto/fallo, la racha actual y la interacción con el servicio de ranking.

### Campos (atributos)

```java
private final StackPane moneda;        // contenedor de la moneda (círculo + imágenes)
private final ImageView estrella;      // imagen de CARA
private final ImageView cruzFig;        // imagen de CRUZ
private final ListView<String> ranking; // lista visual del ranking
private final Button cara, cruz, verR;  // botones de la interfaz
private final String nombre;            // nombre del jugador
private final Random rand = new Random(); // generador aleatorio para el resultado
private int rachaActual = 0;            // racha de aciertos consecutivos
private final RankingService service = new RankingService(); // servicio de persistencia
private boolean animando = false;       // bandera para evitar múltiples animaciones simultáneas
```

**Significado de cada campo:**
- `moneda`: El StackPane que contiene el círculo con gradiente y las dos imágenes apiladas
- `estrella`, `cruzFig`: Referencias a los ImageView para poder ocultar/mostrar según el resultado
- `ranking`: ListView donde se muestra el top 5 actualizado tras cada jugada
- `cara`, `cruz`, `verR`: Botones, necesarios para deshabilitarlos durante la animación
- `nombre`: Se usa al guardar el récord
- `rand`: Genera `true` o `false` aleatoriamente para decidir si sale CARA o CRUZ
- `rachaActual`: Contador incremental de aciertos seguidos
- `service`: Objeto que delega en la base de datos para guardar/recuperar rankings
- `animando`: Evita que el usuario haga clic múltiples veces mientras la moneda está girando

### Constructor

```java
public JuegoController(StackPane moneda, ImageView estrella, ImageView cruzFig,
                       ListView<String> ranking, Button cara, Button cruz,
                       Button verR, String nombre) {
    this.moneda = moneda; this.estrella = estrella; this.cruzFig = cruzFig;
    this.ranking = ranking; this.cara = cara; this.cruz = cruz;
    this.verR = verR; this.nombre = nombre;
}
```

**Propósito:** Recibe e inyecta todas las dependencias visuales desde `Main`. Almacena cada referencia en su campo correspondiente para usarlas en los métodos de juego.

### Método `jugar(String eleccion)`

```java
public void jugar(String eleccion) {
```

**Propósito:** Método principal que ejecuta una jugada completa. Recibe la elección del usuario ("CARA" o "CRUZ").

#### Bloqueo de seguridad

```java
if (animando) return;      // si ya está animando, ignorar clics
animando = true;           // marcar como animando
cara.setDisable(true);     // deshabilitar botones
cruz.setDisable(true);
```

Evita que el usuario acumule jugadas mientras la animación está en curso. Los botones se deshabilitan visualmente.

#### Resultado aleatorio

```java
String resultado = rand.nextBoolean() ? "CARA" : "CRUZ";
```

Usa `Random.nextBoolean()` para generar el resultado. 50% de probabilidad para cada cara.

#### Animación de compresión (squish)

```java
ScaleTransition squish = new ScaleTransition(Duration.millis(600), moneda);
squish.setToY(0.01);
```

**Significado:** Crea una animación que reduce la escala vertical de la moneda a prácticamente cero (0.01) en 600 milisegundos. Esto simula que la moneda se está viendo de canto mientras gira. `ScaleTransition` modifica la propiedad `scaleY` del nodo.

#### Animación de parpadeo (spin)

```java
Timeline spin = new Timeline();
for (int i = 0; i < 10; i++) {
    boolean mostrar = i % 2 == 0;
    spin.getKeyFrames().add(new KeyFrame(Duration.millis(65 * i), e -> {
        estrella.setVisible(mostrar);
        cruzFig.setVisible(!mostrar);
    }));
}
```

**Significado:** Crea una línea de tiempo con 10 fotogramas. En cada fotograma alterna la visibilidad de las dos caras: en fotogramas pares se muestra CARA, en impares CRUZ. Los fotogramas están espaciados 65 milisegundos entre sí, dando un efecto de parpadeo rápido que simula el giro de la moneda.

`Timeline` es una secuencia de `KeyFrame` que se ejecutan en orden temporal. Cada `KeyFrame` contiene un tiempo (`Duration.millis(65 * i)`) y una acción (la lambda que alterna visibilidad).

#### Finalización del giro

```java
spin.setOnFinished(e -> {
    squish.stop();                                      // detener compresión
    boolean ok = eleccion.equals(resultado);             // ¿acertó?
    estrella.setVisible(resultado.equals("CARA"));      // mostrar cara correcta
    cruzFig.setVisible(resultado.equals("CRUZ"));
    moneda.setEffect(new DropShadow(15, 3, 3,           // sombra de resultado
        ok ? Color.web("#4CAF50", 0.6) : Color.web("#F44336", 0.6)));
```

**Significado:** Cuando el Timeline termina (tras los 10 fotogramas):
1. Detiene la animación de compresión (la moneda sigue siendo una línea)
2. Determina si el usuario acertó comparando `eleccion` con `resultado`
3. Muestra solo la imagen correspondiente al resultado real
4. Aplica una sombra brillante: verde (`#4CAF50`) si acertó, roja (`#F44336`) si falló

#### Animación de expansión

```java
ScaleTransition expand = new ScaleTransition(Duration.millis(300), moneda);
expand.setToY(1);
expand.setOnFinished(e2 -> {
    if (ok) {
        rachaActual++;
    } else {
        if (rachaActual > 0) service.guardarSiMejor(nombre, rachaActual);
        rachaActual = 0;
    }
    System.out.printf("[%s] %s -> %s %s (racha:%d)%n",
        nombre, eleccion, resultado, ok ? "OK" : "X", rachaActual);
    ranking.getItems().setAll(
        service.obtenerRanking().getItems().stream()
            .map(p -> String.format("%s - %d", p.getNombre(), p.getRacha()))
            .toList()
    );
    cara.setDisable(false);
    cruz.setDisable(false);
    animando = false;
});
expand.play();
```

**Significado:** Recupera la escala vertical de la moneda a 1 (tamaño normal) en 300ms. Cuando termina:
- Si acertó: incrementa `rachaActual`
- Si falló y tenía racha > 0: guarda la racha en BD solo si supera el récord anterior. Reinicia racha a 0
- Imprime el resultado formateado por consola
- Actualiza el ListView del ranking con los datos de la BD, formateando cada entrada como `"Nombre - Racha"`
- Vuelve a habilitar los botones y marca `animando = false`

```java
squish.play();
spin.play();
```

Inicia ambas animaciones simultáneamente: la moneda se comprime verticalmente mientras las caras parpadean.

### Método `mostrarRanking()`

```java
public void mostrarRanking() {
    var lr = service.obtenerRanking();
    StringBuilder sb = new StringBuilder("=== TOP 5 RANKING ===\n");
    lr.getItems().forEach(p ->
        sb.append(String.format("%-18s %d racha%n", p.getNombre(), p.getRacha())));
    System.out.print(sb);
    ranking.getItems().setAll(
        lr.getItems().stream()
            .map(p -> String.format("%s - %d", p.getNombre(), p.getRacha()))
            .toList()
    );
}
```

**Propósito:** Muestra el ranking actual en consola y en la interfaz gráfica.

**Flujo:**
1. Llama a `service.obtenerRanking()` que devuelve un `ListaResultados<Partida>` con el top 5
2. Construye un `StringBuilder` con cabecera y cada jugada formateada con ancho fijo `%-18s` para alinear nombres
3. Imprime el ranking en consola
4. Actualiza el `ListView<String>` transformando cada `Partida` a texto `"Nombre - Racha"` mediante `stream().map().toList()`

---

## controller.MusicManager — Música de fondo

**Rol:** Clase estática que gestiona la reproducción de música ambiental durante el juego.

### Campos

```java
private static MediaPlayer mp;
```

`MediaPlayer` es el reproductor de JavaFX para archivos multimedia. Es estático para que sea único y accesible desde cualquier lugar sin instanciar la clase.

### Método `iniciar()`

```java
public static void iniciar() {
    try {
        var url = MusicManager.class.getResource("/sound/musica_fondo.wav");
        if (url == null) return;
        mp = new MediaPlayer(new Media(url.toString()));
        mp.setCycleCount(MediaPlayer.INDEFINITE);   // bucle infinito
        mp.setVolume(0.15);                          // volumen 15%
        mp.play();
    } catch (Exception e) {
        System.err.println("Error musica: " + e.getMessage());
    }
}
```

**Propósito:** Busca el archivo WAV en los recursos, crea un `Media` y un `MediaPlayer`, configura reproducción en bucle infinito y volumen bajo (15%), y comienza a reproducir.

**Detalles:**
- `getResource()` localiza el archivo dentro del JAR o classpath
- `MediaPlayer.INDEFINITE` = -1, indica repetición infinita
- `setVolume(0.15)` = 15% de volumen máximo para no molestar
- Todo dentro de try/catch para que si falta el archivo de audio, la app siga funcionando

### Método `detener()`

```java
public static void detener() {
    if (mp != null) { mp.stop(); mp.dispose(); mp = null; }
}
```

**Propósito:** Detiene la reproducción, libera recursos del `MediaPlayer` y lo marca para recolección de basura.

---

## model.Partida — Modelo de datos

**Rol:** Clase que representa una partida guardada en el ranking. Es un POJO (Plain Old Java Object) con dos campos inmutables.

```java
public class Partida implements Serializable {
    private final String nombre;
    private final int racha;

    public Partida(String nombre, int racha) {
        this.nombre = nombre;
        this.racha = racha;
    }

    public String getNombre() { return nombre; }
    public int getRacha() { return racha; }
}
```

**Significado:**
- `Serializable`: Interfaz marcadora que permite que los objetos `Partida` se escriban/lean de un flujo binario, necesario para `RankingSerialDAO` que usa `ObjectOutputStream`
- `final`: Los campos son inmutables — una vez creada una partida no se puede modificar
- `getNombre()` y `getRacha()`: Getters estándar para acceso a los datos

---

## model.ListaResultados — Contenedor genérico

**Rol:** Clase genérica que encapsula una lista de resultados con capacidad de realizar operaciones agregadas.

```java
public class ListaResultados<T extends Partida> {
    private final List<T> items = new ArrayList<>();

    public void añadir(T item) { items.add(item); }
    public List<T> getItems() { return items; }

    public int sumar(ToIntFunction<? super T> extractor) {
        return items.stream().mapToInt(extractor).sum();
    }
}
```

**Significado de la genericidad:**
- `<T extends Partida>`: El tipo `T` debe ser `Partida` o una subclase. Esto permite que la lista solo contenga objetos que son partidas (o subtipos), dando seguridad de tipos en compilación
- `ToIntFunction<? super T>`: Acepta una función que extrae un entero de un objeto de tipo `T` o cualquier superclase. El `? super T` (wildcard contravariante) hace el método más flexible

**Métodos:**
- `añadir(T item)`: Agrega un elemento a la lista interna
- `getItems()`: Devuelve la lista completa (no defensiva — se expone directamente)
- `sumar(ToIntFunction<? super T> extractor)`: Usa `stream().mapToInt().sum()` para sumar todos los valores extraídos de cada elemento. Ejemplo de uso: `lr.sumar(Partida::getRacha)` suma todas las rachas de la lista

**Operaciones agregadas:**
- `stream()` convierte la lista en un flujo de datos
- `mapToInt()` transforma cada elemento a un entero usando el extractor
- `sum()` suma todos los enteros del flujo

---

## dao.RankingDAO — Interfaz de persistencia

**Rol:** Define el contrato que deben cumplir todas las implementaciones de acceso a datos del ranking. Sigue el patrón **DAO (Data Access Object)** que separa la lógica de persistencia de la lógica de negocio.

```java
public interface RankingDAO {
    void insertar(Partida p);
    List<Partida> obtenerTop5();
    int obtenerMejorRacha(String nombre);
}
```

**Métodos:**
- `insertar(Partida p)`: Guarda una nueva partida en el almacenamiento
- `obtenerTop5()`: Devuelve las 5 mejores partidas ordenadas por racha descendente
- `obtenerMejorRacha(String nombre)`: Devuelve la racha más alta de un jugador específico

**Ventajas de usar interfaz:**
1. **Desacoplamiento**: `RankingService` trabaja contra la interfaz, no contra una implementación concreta
2. **Intercambiabilidad**: Podemos cambiar de SQLite a serialización (o cualquier otro sistema) sin modificar el servicio
3. **Testabilidad**: Podemos crear implementaciones mock para pruebas

---

## dao.RankingSQLiteDAO — Implementación SQLite

**Rol:** Implementa `RankingDAO` usando una base de datos SQLite a través de JDBC.

### Método `insertar(Partida p)`

```java
@Override
public void insertar(Partida p) {
    String sql = "INSERT INTO ranking (nombre, racha) VALUES (?, ?)";
    try (Connection c = ConexionDB.obtenerConexion();
         PreparedStatement st = c.prepareStatement(sql)) {
        st.setString(1, p.getNombre());
        st.setInt(2, p.getRacha());
        st.executeUpdate();
    } catch (SQLException e) {
        System.err.println("Error insertar: " + e.getMessage());
    }
}
```

**Propósito:** Inserta una nueva fila en la tabla `ranking`.

**Detalles técnicos:**
- `PreparedStatement`: Consulta parametrizada que previene inyección SQL. Los `?` son marcadores de posición
- `setString(1, ...)` y `setInt(2, ...)`: Sustituyen los marcadores por los valores reales
- `executeUpdate()`: Ejecuta la sentencia de inserción (devuelve el número de filas afectadas)
- **Try-with-resources**: El bloque `try (...)` cierra automáticamente `Connection` y `PreparedStatement` al salir

### Método `obtenerTop5()`

```java
@Override
public List<Partida> obtenerTop5() {
    List<Partida> lista = new ArrayList<>();
    try (Connection c = ConexionDB.obtenerConexion();
         Statement st = c.createStatement();
         ResultSet rs = st.executeQuery(
             "SELECT nombre, racha FROM ranking ORDER BY racha DESC LIMIT 5")) {
        while (rs.next())
            lista.add(new Partida(rs.getString("nombre"), rs.getInt("racha")));
    } catch (SQLException e) {
        System.err.println("Error listar: " + e.getMessage());
    }
    return lista;
}
```

**Propósito:** Recupera las 5 mejores rachas ordenadas de mayor a menor.

**Detalles técnicos:**
- `Statement`: Usa `Statement` (no preparado) porque no tiene parámetros
- `ResultSet`: Cursor que recorre las filas devueltas. `rs.next()` avanza a la siguiente fila y devuelve `false` cuando no hay más
- `ORDER BY racha DESC`: Orden descendente por racha
- `LIMIT 5`: Solo las 5 primeras filas
- Cada fila se convierte a objeto `Partida` con `rs.getString("nombre")` y `rs.getInt("racha")`

### Método `obtenerMejorRacha(String nombre)`

```java
@Override
public int obtenerMejorRacha(String nombre) {
    String sql = "SELECT COALESCE(MAX(racha), 0) FROM ranking WHERE nombre = ?";
    try (Connection c = ConexionDB.obtenerConexion();
         PreparedStatement st = c.prepareStatement(sql)) {
        st.setString(1, nombre);
        ResultSet rs = st.executeQuery();
        if (rs.next()) return rs.getInt(1);
    } catch (SQLException e) {
        System.err.println("Error consulta: " + e.getMessage());
    }
    return 0;
}
```

**Propósito:** Obtiene la racha máxima de un jugador. Si el jugador no existe, devuelve 0.

**Detalles técnicos:**
- `MAX(racha)`: Función de agregación SQL que devuelve el valor máximo
- `COALESCE(MAX(racha), 0)`: Si `MAX(racha)` es NULL (jugador no existe), devuelve 0
- `WHERE nombre = ?`: Filtra por nombre de jugador
- `rs.getInt(1)`: Obtiene el valor de la primera columna del resultado

---

## dao.RankingSerialDAO — Implementación serialización

**Rol:** Implementa `RankingDAO` usando serialización Java para almacenar los datos en un archivo binario (`ranking_oo.dat`).

### Campo

```java
private static final String FICHERO = "ranking_oo.dat";
```

Nombre del archivo donde se guardan los datos. `static final` = constante de clase.

### Método `insertar(Partida p)`

```java
@Override
public void insertar(Partida p) {
    List<Partida> lista = leerTodos();
    lista.add(p);
    escribirTodos(lista);
}
```

**Propósito:** Añade una partida al archivo. Lee todas las existentes, añade la nueva y las vuelve a escribir todas (no hay inserción directa como en SQL).

### Método `obtenerTop5()`

```java
@Override
public List<Partida> obtenerTop5() {
    return leerTodos().stream()
            .sorted(Comparator.comparingInt(Partida::getRacha).reversed())
            .limit(5).collect(Collectors.toList());
}
```

**Propósito:** Filtra y ordena en memoria usando streams:
1. `leerTodos()` → lista completa
2. `.stream()` → flujo de datos
3. `.sorted(Comparator.comparingInt(Partida::getRacha).reversed())` → ordena por racha descendente
4. `.limit(5)` → toma solo las 5 primeras
5. `.collect(Collectors.toList())` → convierte el flujo de vuelta a lista

### Método `obtenerMejorRacha(String nombre)`

```java
@Override
public int obtenerMejorRacha(String nombre) {
    return leerTodos().stream()
            .filter(p -> p.getNombre().equals(nombre))
            .mapToInt(Partida::getRacha)
            .max().orElse(0);
}
```

**Propósito:** Filtra las partidas del jugador, extrae las rachas, encuentra el máximo y si no hay ninguna devuelve 0.

### Método privado `leerTodos()`

```java
@SuppressWarnings("unchecked")
private List<Partida> leerTodos() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHERO))) {
        return (List<Partida>) ois.readObject();
    } catch (Exception e) { return new ArrayList<>(); }
}
```

**Propósito:** Deserializa la lista completa desde el archivo binario.

**Detalles técnicos:**
- `ObjectInputStream`: Lee objetos Java desde un flujo binario
- `FileInputStream`: Lee bytes desde un archivo
- `readObject()`: Deserializa el objeto (devuelve `Object`, hay que castear)
- `@SuppressWarnings("unchecked")`: Suprime la advertencia por el cast potencialmente inseguro
- Si falla (archivo no existe, formato incorrecto), captura la excepción y devuelve lista vacía

### Método privado `escribirTodos(List<Partida> lista)`

```java
private void escribirTodos(List<Partida> lista) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHERO))) {
        oos.writeObject(lista);
    } catch (Exception e) {
        System.err.println("Error guardando: " + e.getMessage());
    }
}
```

**Propósito:** Serializa la lista completa al archivo binario.

**Detalles técnicos:**
- `ObjectOutputStream`: Escribe objetos Java a un flujo binario
- `FileOutputStream`: Escribe bytes a un archivo
- `writeObject(lista)`: Serializa la lista entera (requiere que `Partida` implemente `Serializable`)
- Si falla (permisos, espacio), muestra el error pero no interrumpe la ejecución

---

## database.ConexionDB — Conexión a base de datos

**Rol:** Clase utilitaria que proporciona acceso a la base de datos SQLite y crea la tabla si no existe.

### Campo

```java
private static String url = "jdbc:sqlite:ranking.db";
```

URL de conexión JDBC a SQLite. `ranking.db` es el archivo de base de datos que se crea en el directorio de trabajo.

### Método `obtenerConexion()`

```java
public static Connection obtenerConexion() throws SQLException {
    return DriverManager.getConnection(url);
}
```

**Propósito:** Devuelve una nueva conexión JDBC a la base de datos SQLite. `DriverManager.getConnection()` carga automáticamente el driver SQLite si está en el classpath (dependencia `sqlite-jdbc` en pom.xml).

### Método `crearTabla()`

```java
public static void crearTabla() {
    try (Connection c = obtenerConexion(); Statement st = c.createStatement()) {
        st.execute("CREATE TABLE IF NOT EXISTS ranking (" +
                   "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                   "nombre TEXT NOT NULL, racha INTEGER NOT NULL)");
    } catch (SQLException e) {
        System.err.println("Error BD: " + e.getMessage());
    }
}
```

**Propósito:** Crea la tabla `ranking` si no existe.

**Esquema SQL:**
- `id` → entero, clave primaria con auto-incremento (SQLite asigna 1, 2, 3...)
- `nombre` → texto, no nulo
- `racha` → entero, no nulo

`IF NOT EXISTS` evita errores si la tabla ya existe.

---

## service.RankingService — Capa de negocio

**Rol:** Intermediario entre los controladores y los DAO. Contiene la lógica de negocio del ranking.

### Campo

```java
private final RankingDAO dao = new RankingSQLiteDAO();
```

Se inyecta la implementación SQLite. Por ser `RankingDAO` (interfaz), se puede cambiar fácilmente a `RankingSerialDAO` sin modificar el resto del código.

### Método `guardarRecord(String nombre, int racha)`

```java
public void guardarRecord(String nombre, int racha) {
    dao.insertar(new Partida(nombre, racha));
}
```

Inserta directamente una nueva partida sin comprobaciones.

### Método `guardarSiMejor(String nombre, int racha)`

```java
public void guardarSiMejor(String nombre, int racha) {
    if (racha > obtenerMejorRacha(nombre)) {
        dao.insertar(new Partida(nombre, racha));
    }
}
```

**Lógica de negocio:** Solo guarda el récord si la racha actual supera la mejor racha anterior del jugador. Así no se llena la BD con récords que no mejoran la marca personal.

### Método `obtenerMejorRacha(String nombre)`

```java
public int obtenerMejorRacha(String nombre) {
    return dao.obtenerMejorRacha(nombre);
}
```

**Delega:** Simplemente pasa la llamada al DAO.

### Método `obtenerRanking()`

```java
public ListaResultados<Partida> obtenerRanking() {
    ListaResultados<Partida> lr = new ListaResultados<>();
    dao.obtenerTop5().forEach(lr::aadir);
    System.out.println("Suma total de rachas TOP5: " + lr.sumar(Partida::getRacha));
    return lr;
}
```

**Propósito:** Obtiene el top 5, lo envuelve en un `ListaResultados` y además muestra por consola la suma total de rachas usando el método genérico `sumar()`.

**Flujo:**
1. Crea un `ListaResultados<Partida>` vacío
2. Obtiene el top 5 del DAO y los añade con `forEach(lr::añadir)` (method reference)
3. Usa `lr.sumar(Partida::getRacha)` para calcular la suma total (operación agregada)
4. Imprime la suma por consola
5. Devuelve el `ListaResultados`

---

## css/estilo.css — Estilos visuales

**Rol:** Hoja de estilos CSS aplicada a la escena JavaFX.

```css
.boton-juego {
    -fx-text-fill: white;           /* texto blanco */
    -fx-font-weight: bold;           /* negrita */
    -fx-background-radius: 10;       /* esquinas redondeadas */
    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 6, 0, 0, 3);
}
.boton-juego:hover {
    -fx-text-fill: black;           /* al pasar el ratón: texto negro */
}
```

**Propiedades CSS explicadas:**
- `-fx-text-fill`: Color del texto
- `-fx-font-weight`: Grosor de la fuente
- `-fx-background-radius`: Radio de las esquinas del botón (10px)
- `-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 6, 0, 0, 3)`:
  - `gaussian`: Tipo de desenfoque
  - `rgba(0,0,0,0.4)`: Color negro al 40% de opacidad
  - `6`: Radio de desenfoque
  - `0`: Desplazamiento X
  - `0`: Desplazamiento Y
  - `3`: Radio de propagación

La pseudo-clase `:hover` cambia el texto a negro cuando el ratón está sobre el botón.

---

## BackendTest — Tests unitarios

**Rol:** Pruebas unitarias JUnit 5 que verifican el funcionamiento del modelo, los DAOs y el servicio.

### Configuración

```java
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
}
```

**Detalles:**
- `@TestMethodOrder(MethodOrderer.OrderAnnotation.class)`: Ejecuta los tests en el orden especificado por `@Order`
- `@BeforeAll`: Se ejecuta una vez antes de todos los tests
- `static`: Los campos y el setup son estáticos porque `@BeforeAll` requiere método estático
- Limpia la tabla `ranking` al inicio para evitar datos residuales

### testPartidaModelo

```java
@Test @Order(1)
void testPartidaModelo() {
    Partida p = new Partida("Ana", 5);
    assertEquals("Ana", p.getNombre());
    assertEquals(5, p.getRacha());
}
```

Verifica que el constructor y los getters de `Partida` funcionan correctamente.

### testSQLiteInsertarYRecuperar

```java
@Test @Order(2)
void testSQLiteInsertarYRecuperar() {
    sqlite.insertar(new Partida("Test1", 10));
    List<Partida> ranking = sqlite.obtenerTop5();
    assertTrue(ranking.stream().anyMatch(
        p -> p.getNombre().equals("Test1") && p.getRacha() == 10));
}
```

Inserta una partida en SQLite y verifica que aparece en el top 5.

### testSQLiteTop5Ordenado

```java
@Test @Order(3)
void testSQLiteTop5Ordenado() {
    for (int i = 0; i < 6; i++)
        sqlite.insertar(new Partida("P" + i, i));
    List<Partida> top = sqlite.obtenerTop5();
    assertEquals(5, top.size());
    for (int i = 1; i < top.size(); i++)
        assertTrue(top.get(i - 1).getRacha() >= top.get(i).getRacha());
}
```

Inserta 6 partidas y verifica que solo devuelve 5 y que están ordenadas de mayor a menor racha.

### testSerialInsertarYRecuperar

```java
@Test @Order(4)
void testSerialInsertarYRecuperar() {
    serial.insertar(new Partida("Serial1", 7));
    List<Partida> ranking = serial.obtenerTop5();
    assertTrue(ranking.stream().anyMatch(
        p -> p.getNombre().equals("Serial1") && p.getRacha() == 7));
}
```

Verifica el mismo comportamiento con `RankingSerialDAO`.

### testService

```java
@Test @Order(5)
void testService() {
    RankingService svc = new RankingService();
    svc.guardarRecord("Service1", 3);
    assertTrue(svc.obtenerRanking().getItems().stream()
        .anyMatch(p -> p.getNombre().equals("Service1")));
}
```

Integra `RankingService` con el DAO (SQLite) y verifica que guardar y recuperar funcionan de principio a fin.

---

## Flujo completo de ejecución

```
Inicio (main)
  │
  ├─ Pedir nombre por consola (Scanner)
  ├─ Guardar nombre en propiedad del sistema
  └─ launch(args)
       │
       └─ start(Stage)
            │
            ├─ Crear tabla SQLite (si no existe)
            ├─ Iniciar música de fondo (WAV en bucle)
            ├─ Crear gradiente radial (colores de las imágenes)
            ├─ Crear círculo con gradiente + borde + sombra
            ├─ Cargar imágenes cara.png y cruz.png
            ├─ Crear StackPane con círculo + imágenes
            ├─ Crear botones (CARA, CRUZ, VER RANKING)
            ├─ Crear ListView para ranking
            ├─ Crear JuegoController (inyectando todos los nodos)
            ├─ Asignar eventos a botones
            │    ├─ cara → ctrl.jugar("CARA")
            │    ├─ cruz → ctrl.jugar("CRUZ")
            │    └─ verR → ctrl.mostrarRanking()
            ├─ Montar layout (VBox > HBox + moneda + ranking)
            ├─ Crear escena con fondo degradado
            ├─ Configurar Stage: fullscreen, título, icono
            └─ show()

Jugar (elección del usuario)
  │
  ├─ Bloquear botones (evitar clics múltiples)
  ├─ Generar resultado aleatorio (CARA/CRUZ)
  ├─ Iniciar animación:
  │    ├─ ScaleTransition: comprimir moneda (scaleY → 0.01)
  │    └─ Timeline: alternar imágenes cada 65ms (10 fotogramas)
  │
  └─ Al finalizar Timeline:
       ├─ Detener compresión
       ├─ Mostrar imagen del resultado real
       ├─ Aplicar sombra: verde (acierto) / roja (fallo)
       ├─ Iniciar expansión (scaleY → 1)
       │
       └─ Al finalizar expansión:
            ├─ Acertó   → rachaActual++
            ├─ Falló    → guardar si es récord personal, racha = 0
            ├─ Imprimir resultado por consola
            ├─ Actualizar ListView del ranking
            ├─ Desbloquear botones
            └─ Fin
```

---

## Diagrama de dependencias

```
Main (app)
  │
  ├──► ConexionDB (database)
  │       └── JDBC → SQLite
  │
  ├──► MusicManager (controller)
  │       └── JavaFX MediaPlayer → WAV
  │
  └──► JuegoController (controller)
          │
          ├──► RankingService (service)
          │       │
          │       └──► RankingDAO (interfaz - dao)
          │               │
          │               ├──► RankingSQLiteDAO
          │               │       └──► ConexionDB
          │               │
          │               └──► RankingSerialDAO
          │                       └── ObjectStreams → ranking_oo.dat
          │
          └──► Modelos (model)
                  ├── Partida (Serializable)
                  └── ListaResultados<T extends Partida>

BackendTest (test)
  ├──► ConexionDB
  ├──► RankingSQLiteDAO
  ├──► RankingSerialDAO
  └──► RankingService
```

**Relaciones clave:**
- `Main` → `JuegoController` (composición): El controlador recibe los nodos gráficos ya creados
- `JuegoController` → `RankingService` (composición): El controlador usa el servicio para persistencia
- `RankingService` → `RankingDAO` (interfaz): Programación contra interfaz, no contra implementación
- `RankingSQLiteDAO` y `RankingSerialDAO` → `RankingDAO` (implementación): Dos estrategias de persistencia intercambiables
- `ConexionDB` es utilitaria y usada por `RankingSQLiteDAO` y los tests
- `Partida` es el modelo de datos compartido entre todas las capas
- `ListaResultados` es el contenedor genérico usado por `RankingService`
