package org.uabc.ic;

/**
 * Interfaz que define las operaciones para elementos que pueden ser gestionados dentro de un inventario.
 */
public interface Inventariable {

    /**
     * Registra el elemento en el inventario.
     */
    void registrar();

    /**
     * Elimina el elemento del inventario.
     */
    void borrar();
}