package org.uabc.ic;

/**
 * Representa objetos interactivos y dinámicos del escenario (ej. barriles, cajas).
 */
public class Utileria implements ElementoDinamico {
    private String nombre;
    private String descripcion;
    private int posicionX;
    private int posicionY;
    private boolean usada;

    public Utileria(String nombre, String descripcion, int posicionX, int posicionY) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.usada = false;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getPosicionX() { return posicionX; }
    public int getPosicionY() { return posicionY; }
    public boolean isUsada() { return usada; }

    public void usar() {
        this.usada = true;
        System.out.println("[UTILIZADO] Se usó el contenedor: " + nombre);
    }

    @Override
    public void mover(String direccion, int distancia) {
        actualizarCoordenadas(direccion, distancia);
        System.out.println("[DESPLAZADO] Contenedor " + nombre + " movido a (" + posicionX + ", " + posicionY + ")");
    }

    private void actualizarCoordenadas(String direccion, int distancia) {
        switch (direccion.toLowerCase()) {
            case "norte": case "arriba": posicionY -= distancia; break;
            case "sur": case "abajo": posicionY += distancia; break;
            case "este": case "derecha": posicionX += distancia; break;
            case "oeste": case "izquierda": posicionX -= distancia; break;
        }
    }
}