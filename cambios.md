# Cambios realizados

## Motivo
Eliminación de criterios RA 6.f y RA 6.j, y simplificación visual del código.

---

## Archivos modificados

### `src/main/java/app/Main.java`
- Eliminado `RadialGradient`, `DropShadow`, `Color`, `CycleMethod`, `Stop` (movido a CSS)
- Círculo simplificado: `new Circle(160)` + clase CSS `.moneda-circulo`
- Todos los estilos inline (`setStyle`) reemplazados por clases CSS
- Eliminado `import javafx.geometry.Pos`
- **110 → 89 líneas**

### `src/main/java/controller/JuegoController.java`
- Reemplazados `.stream().map(...).toList()` por bucles `for` con `ArrayList`
- Añadidos `import model.Partida` e `import java.util.ArrayList`

### `src/main/java/service/RankingService.java`
- `obtenerRanking()` ahora devuelve `List<Partida>` directamente
- Eliminada dependencia de `ListaResultados`

### `src/main/java/model/ListaResultados.java`
- **Eliminado** (ya no se necesita)

### `src/test/java/BackendTest.java`
- Reemplazados `.stream().anyMatch()` por bucles `for` con flag

### `src/main/resources/css/estilo.css`
- Añadido `.moneda-circulo` con `radial-gradient`, stroke y dropshadow
- Añadido `.racha-label`, `.btn-cara`, `.btn-ranking`, `.btn-cruz`
- Añadido `.fila-botones`, `.root-panel`
- Mejorado `.ranking-list` con borde, zebra striping, hover, selección, scrollbar
- Letras del ranking más grandes (16) y centradas

### `DOCUMENTACION.md`
- Eliminadas todas las referencias a `ListaResultados`, genéricos y streams

### `README.md`
- Eliminadas características de genéricos y streams
- Eliminada referencia a `ListaResultados` en la estructura
