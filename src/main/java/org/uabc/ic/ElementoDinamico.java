package org.uabc.ic;

/**
 * Interfaz que define la capacidad de movimiento para los elementos dinámicos del escenario.
 */
public interface ElementoDinamico {

    /**
     * Desplaza el elemento en una dirección y distancia específicas.
     *
     * @param direccion Dirección del movimiento (ej. "norte", "sur", "este", "oeste").
     * @param distancia Unidades de distancia a desplazarse.
     */
    void mover(String direccion, int distancia);
}