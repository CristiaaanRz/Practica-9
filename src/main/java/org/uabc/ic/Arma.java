package org.uabc.ic;

/**
 * Representa un arma que puede ser equipada o guardada en el inventario.
 */
public class Arma implements Inventariable {

    /** Nombre identificador del arma. */
    private String nombre;

    /** Puntos de daño que inflige el arma. */
    private int daño;

    /** Alcance máximo del disparo en la cuadrícula. */
    private double alcance;

    /**
     * Inicializa un arma con sus propiedades de combate.
     */
    public Arma(String nombre, int daño, double alcance) {
        this.nombre = nombre;
        this.daño = daño;
        this.alcance = alcance;
    }

    /**
     * Obtiene el nombre del arma.
     */
    public String getNombre() { return nombre; }

    /**
     * Obtiene los puntos de daño.
     */
    public int getDaño() { return daño; }

    /**
     * Obtiene la distancia de alcance del disparo.
     */
    public double getAlcance() { return alcance; }

    /**
     * Notifica cuando el arma es agregada o equipada en la mochila.
     */
    public void registrar() {
        System.out.println("[EQUIPADO] Arma lista: " + nombre + " (Alcance: " + alcance + "m)");
    }

    /**
     * Notifica cuando el arma es descartada del inventario.
     */
    public void borrar() {
        System.out.println("[DESECHADO] Arma retirada: " + nombre);
    }
}