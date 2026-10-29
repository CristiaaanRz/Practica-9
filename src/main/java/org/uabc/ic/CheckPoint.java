package org.uabc.ic;

/**
 * Representa un punto de guardado en el nivel.
 */
public class CheckPoint {
    private String nombre;
    private int posicionX;
    private int posicionY;
    private boolean activado;

    public CheckPoint(String nombre, int posicionX, int posicionY) {
        this.nombre = nombre;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.activado = false;
    }

    public String getNombre() { return nombre; }
    public int getPosicionX() { return posicionX; }
    public int getPosicionY() { return posicionY; }
    public boolean isActivado() { return activado; }

    public void activar() {
        this.activado = true;
        System.out.println("[TERMINAL] Punto de control activado: " + nombre + " en (" + posicionX + ", " + posicionY + ")");
    }
}