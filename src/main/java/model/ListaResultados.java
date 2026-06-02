package model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

public class ListaResultados<T extends Partida> {
    private final List<T> items = new ArrayList<>();

    public void añadir(T item) { items.add(item); }
    public List<T> getItems() { return items; }

    public int sumar(ToIntFunction<? super T> extractor) {
        return items.stream().mapToInt(extractor).sum();
    }
}
