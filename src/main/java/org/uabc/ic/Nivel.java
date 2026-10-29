package org.uabc.ic;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un sector o nivel del juego con sus entidades y elementos.
 */
public class Nivel {

    private String nombre;
    private int numero;
    private String dificultad;
    private List<Obstaculo> obstaculos;
    private List<CheckPoint> checkPoints;
    private List<ElementoDinamico> elementosDinamicos;
    private Inventario inventario;

    /**
     * Inicializa un nuevo nivel con sus atributos e inventario.
     */
    public Nivel(String nombre, int numero, String dificultad, Inventario inventario) {
        this.nombre = nombre;
        this.numero = numero;
        this.dificultad = dificultad;
        this.inventario = inventario;
        this.obstaculos = new ArrayList<>();
        this.checkPoints = new ArrayList<>();
        this.elementosDinamicos = new ArrayList<>();
    }

    /**
     * Obtiene el nombre del nivel.
     */
    public String getNombre() { return nombre; }

    /**
     * Obtiene el número de sector.
     */
    public int getNumero() { return numero; }

    /**
     * Obtiene la dificultad configurada.
     */
    public String getDificultad() { return dificultad; }

    /**
     * Agrega un obstáculo a la lista del escenario.
     */
    public void agregarObstaculo(Obstaculo obstaculo) {
        obstaculos.add(obstaculo);
    }

    /**
     * Registra un nuevo punto de control en el nivel.
     */
    public void agregarCheckPoint(CheckPoint checkPoint) {
        checkPoints.add(checkPoint);
    }

    /**
     * Añade un elemento móvil al nivel.
     */
    public void agregarElementoDinamico(ElementoDinamico elemento) {
        elementosDinamicos.add(elemento);
    }

    /**
     * Desplaza todos los elementos dinámicos registrados.
     */
    public void moverElementosDinamicos(String direccion, int distancia) {
        System.out.println("\n--- Moviendo elementos dinámicos del sector ---");
        for (ElementoDinamico elemento : elementosDinamicos) {
            elemento.mover(direccion, distancia);
        }
    }

    /**
     * Muestra en consola el resumen y estado actual del nivel.
     */
    public void mostrarEstado() {
        System.out.println("\n========================================");
        System.out.println(" SECTOR " + numero + ": " + nombre + " [" + dificultad + "]");
        System.out.println("========================================");
        System.out.println("Fugas/Peligros: " + obstaculos.size());
        System.out.println("Terminales de Control: " + checkPoints.size());
        System.out.println("Elementos Móviles: " + elementosDinamicos.size());
        inventario.listarItems();
        System.out.println("========================================\n");
    }
}