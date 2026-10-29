package org.uabc.ic;

/**
 * Clase principal que ejecuta la simulación del videojuego.
 */
public class Main {

    /**
     * Método de entrada principal.
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        System.out.println("=== INICIANDO SIMULACIÓN DEL VIDEOJUEGO ===");

        Inventario inventarioNivel = new Inventario(5);
        Arma espada = new Arma("Espada Legendaria", 45, 2.5);
        Recompensa gema = new Recompensa("Gema de Fuego", 500, "Experiencia");

        inventarioNivel.agregarItem(espada);
        inventarioNivel.agregarItem(gema);

        Nivel nivel1 = new Nivel("Bosque Encantado", 1, "Normal", inventarioNivel);

        Obstaculo trampa = new Obstaculo("Trampa de Espinas", 20, 5, 12);
        CheckPoint cp1 = new CheckPoint("Checkpoint Inicial", 0, 0);

        nivel1.agregarObstaculo(trampa);
        nivel1.agregarCheckPoint(cp1);

        Personaje heroe = new Personaje("Héroe", 100, 0, 0);
        Utileria barril = new Utileria("Barril Rodante", "Barril de madera", 10, 5);

        nivel1.agregarElementoDinamico(heroe);
        nivel1.agregarElementoDinamico(barril);

        nivel1.mostrarEstado();

        nivel1.moverElementosDinamicos("norte", 1);

        nivel1.mostrarEstado();
    }
}