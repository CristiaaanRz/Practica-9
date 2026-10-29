package org.uabc.ic;

/**
 * Representa a un personaje en el juego capaz de desplazarse y recibir daño.
 */
public class Personaje implements Destruible, ElementoDinamico {
    private String nombre;
    private int vida;
    private int posicionX;
    private int posicionY;

    public Personaje(String nombre, int vida, int posicionX, int posicionY) {
        this.nombre = nombre;
        this.vida = vida;
        this.posicionX = posicionX;
        this.posicionY = posicionY;
    }

    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public int getPosicionX() { return posicionX; }
    public int getPosicionY() { return posicionY; }

    public void setPosicion(int x, int y) {
        this.posicionX = x;
        this.posicionY = y;
    }

    public void recibirDaño(int daño) {
        this.vida -= daño;
        System.out.println("[ALERTA TRAJE] " + nombre + " recibió " + daño + " de daño. Oxígeno/Vida: " + vida);
        if (this.vida <= 0) {
            destruye();
        }
    }

    @Override
    public void mover(String direccion, int distancia) {
        switch (direccion.toLowerCase()) {
            case "norte": case "arriba":
                if (posicionY - distancia >= 0) posicionY -= distancia;
                break;
            case "sur": case "abajo":
                if (posicionY + distancia < 15) posicionY += distancia;
                break;
            case "este": case "derecha":
                if (posicionX + distancia < 15) posicionX += distancia;
                break;
            case "oeste": case "izquierda":
                if (posicionX - distancia >= 0) posicionX -= distancia;
                break;
        }
        System.out.println(nombre + " se movió a (" + posicionX + ", " + posicionY + ")");
    }

    @Override
    public void destruye() {
        System.out.println("[CRÍTICO] " + nombre + " se ha quedado sin soporte vital.");
    }

    public void setPosicionX(int nX) {
    }

    public void setPosicionY(int nY) {
    }
}