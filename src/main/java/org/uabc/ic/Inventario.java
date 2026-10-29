package org.uabc.ic;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona el almacenamiento de objetos del personaje.
 */
public class Inventario {

    /**
     * Capacidad máxima de almacenamiento.
     */
    private int capacidadMaxima;

    /**
     * Lista de elementos guardados en la mochila.
     */
    private List<Inventariable> items;

    /**
     * Inicializa el inventario con el límite especificado.
     */
    public Inventario(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.items = new ArrayList<>();
    }

    /**
     * Obtiene la capacidad máxima de la mochila.
     */
    public int getCapacidadMaxima() { return capacidadMaxima; }

    /**
     * Obtiene los objetos guardados.
     */
    public List<Inventariable> getItems() { return items; }

    /**
     * Agrega un objeto si hay espacio disponible.
     */
    public boolean agregarItem(Inventariable item) {
        if (items.size() < capacidadMaxima) {
            items.add(item);
            item.registrar();
            return true;
        }
        System.out.println("[ALERTA] Mochila llena.");
        return false;
    }

    /**
     * Elimina un objeto de la lista si se encuentra presente.
     */
    public boolean eliminarItem(Inventariable item) {
        if (items.contains(item)) {
            items.remove(item);
            item.borrar();
            return true;
        }
        return false;
    }

    /**
     * Muestra en consola los objetos guardados en la mochila.
     */
    public void listarItems() {
        System.out.println("--- MOCHILA DEL TRAJE ---");
        for (Inventariable item : items) {
            System.out.println("- " + item.getClass().getSimpleName());
        }
    }
}