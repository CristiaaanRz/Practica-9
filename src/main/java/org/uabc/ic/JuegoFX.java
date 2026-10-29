package org.uabc.ic;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Aplicación principal del juego en JavaFX que gestiona la interfaz gráfica,
 * el bucle principal, la renderización del tablero y los eventos de teclado.
 */
public class JuegoFX extends Application {

    private static final int TAMANO_CELDA = 42;
    private static final int GRID_ANCHO = 15;
    private static final int GRID_ALTO = 15;

    private Personaje astronauta;
    private CheckPoint reactorSalida;
    private CheckPoint checkPointIntermedio;
    private Inventario mochila;
    private Nivel sector1;

    private List<Obstaculo> muros;
    private List<Utileria> cajasMovibles;
    private List<Alien> aliens;
    private List<Interruptor> interruptores;
    private List<Proyectil> proyectiles;

    private String direccionMirando = "este";
    private int indiceObjetoEquipado = 0;
    private boolean juegoTerminado = false;
    private boolean victoria = false;
    private String mensajeLog = "¡Nave en crisis! Activa un interruptor [I] para guardar en CheckPoint [C1].";

    private Canvas canvas;
    private ProgressBar barVida;
    private Label lblInfoJugador;
    private Label lblMochila;
    private Label lblLog;

    /**
     * Representa a los enemigos alienígenas del mapa.
     */
    private static class Alien {
        int x, y, dx = 1, daño = 20;
        Alien(int x, int y) { this.x = x; this.y = y; }
    }

    /**
     * Representa las terminales de interruptor con temporizador.
     */
    private static class Interruptor {
        int x, y;
        boolean activado = false;
        boolean enProceso = false;
        long tiempoInicioProceso = 0;
        int segundosRestantes = 10;

        Interruptor(int x, int y) { this.x = x; this.y = y; }
    }

    /**
     * Representa los disparos realizados por el personaje.
     */
    private static class Proyectil {
        int x, y, dirX, dirY, alcanceRestante;
        Proyectil(int x, int y, int dirX, int dirY, int alcance) {
            this.x = x; this.y = y; this.dirX = dirX; this.dirY = dirY;
            this.alcanceRestante = alcance;
        }
    }

    /**
     * Punto de entrada de la aplicación JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        inicializarModeloUML();
        inicializarMapeoEscenario();
        BorderPane root = crearEstructuraUI();

        Scene scene = new Scene(root);
        configurarEventosTeclado(scene);
        iniciarGameLoop();

        primaryStage.setTitle("Supervivencia Espacial - Módulo Alpha");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Instancia las clases principales del modelo lógico.
     */
    private void inicializarModeloUML() {
        mochila = new Inventario(4);
        mochila.agregarItem(new Arma("Cortador Plasma", 40, 3.0));
        mochila.agregarItem(new Recompensa("Poción O2", 30, "Oxígeno"));
        mochila.agregarItem(new Recompensa("Botiquín Médico", 50, "Curación"));

        sector1 = new Nivel("Laberinto", 1, "Difícil", mochila);
        astronauta = new Personaje("Cris", 100, 1, 1);

        reactorSalida = new CheckPoint("Portal de Escape", 13, 13);
        checkPointIntermedio = new CheckPoint("Punto Respawn", 7, 7);

        sector1.agregarElementoDinamico(astronauta);
        sector1.agregarCheckPoint(reactorSalida);
        sector1.agregarCheckPoint(checkPointIntermedio);
    }

    /**
     * Coloca los elementos y entidades en la grilla del mapa.
     */
    private void inicializarMapeoEscenario() {
        muros = new ArrayList<>();
        cajasMovibles = new ArrayList<>();
        aliens = new ArrayList<>();
        interruptores = new ArrayList<>();
        proyectiles = new ArrayList<>();

        for (int i = 0; i < GRID_ANCHO; i++) {
            muros.add(new Obstaculo("Muro Exterior", 0, i, 0));
            muros.add(new Obstaculo("Muro Exterior", 0, i, GRID_ALTO - 1));
        }
        for (int j = 1; j < GRID_ALTO - 1; j++) {
            muros.add(new Obstaculo("Muro Exterior", 0, 0, j));
            muros.add(new Obstaculo("Muro Exterior", 0, GRID_ANCHO - 1, j));
        }

        int[][] paredes = {
                {3,1},{3,2},{3,3},{3,4},{1,6},{2,6},{3,6},{4,6},
                {7,1},{7,2},{7,3},{7,5},{7,6},
                {10,3},{10,4},{10,5},{11,5},{12,5},
                {3,9},{3,10},{3,11},{5,12},{6,12},{7,12},{8,12},
                {11,8},{11,9},{11,10},{11,11}
        };
        for (int[] p : paredes) {
            Obstaculo m = new Obstaculo("Muro Blindado", 0, p[0], p[1]);
            muros.add(m);
            sector1.agregarObstaculo(m);
        }

        cajasMovibles.add(new Utileria("Contenedor A", "Bloque pesados", 5, 3));
        cajasMovibles.add(new Utileria("Contenedor B", "Bloque pesados", 9, 10));
        for (Utileria c : cajasMovibles) sector1.agregarElementoDinamico(c);

        aliens.add(new Alien(2, 5));
        aliens.add(new Alien(8, 4));
        aliens.add(new Alien(4, 10));

        interruptores.add(new Interruptor(1, 13));
        interruptores.add(new Interruptor(13, 1));
    }

    /**
     * Construye y organiza los componentes visuales de la interfaz.
     */
    private BorderPane crearEstructuraUI() {
        BorderPane root = new BorderPane();
        canvas = new Canvas(GRID_ANCHO * TAMANO_CELDA, GRID_ALTO * TAMANO_CELDA);

        barVida = new ProgressBar(1.0);
        barVida.setPrefWidth(150);
        barVida.setStyle("-fx-accent: #10b981;");

        lblInfoJugador = new Label();
        lblMochila = new Label();
        lblLog = new Label();

        lblInfoJugador.setFont(Font.font("Consolas", FontWeight.BOLD, 13));
        lblMochila.setFont(Font.font("Consolas", 12));
        lblLog.setFont(Font.font("Consolas", 12));

        lblInfoJugador.setTextFill(Color.WHITE);
        lblMochila.setTextFill(Color.GOLD);
        lblLog.setTextFill(Color.LIGHTGREEN);

        HBox barArriba = new HBox(15, new Label("VIDA:"), barVida, lblInfoJugador);
        barArriba.setStyle("-fx-padding: 8; -fx-background-color: #0d1117;");
        ((Label)barArriba.getChildren().get(0)).setTextFill(Color.WHITE);

        VBox panelInferior = new VBox(6, barArriba, lblMochila, lblLog);
        panelInferior.setStyle("-fx-padding: 10; -fx-background-color: #161b22;");

        root.setCenter(canvas);
        root.setBottom(panelInferior);
        return root;
    }

    /**
     * Asigna las acciones a realizar al presionar las teclas del teclado.
     */
    private void configurarEventosTeclado(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (juegoTerminado) return;

            KeyCode key = event.getCode();

            if (key == KeyCode.UP || key == KeyCode.W) moverPersonaje("norte", 0, -1);
            else if (key == KeyCode.DOWN || key == KeyCode.S) moverPersonaje("sur", 0, 1);
            else if (key == KeyCode.LEFT || key == KeyCode.A) moverPersonaje("oeste", -1, 0);
            else if (key == KeyCode.RIGHT || key == KeyCode.D) moverPersonaje("este", 1, 0);
            else if (key == KeyCode.TAB || key == KeyCode.Q) cambiarObjetoEquipado();
            else if (key == KeyCode.R) usarObjetoEquipado();
            else if (key == KeyCode.SPACE) dispararArma();
            else if (key == KeyCode.E) interactuarInterruptorOCaja();

            verificarEstadoReglas();
            renderizar();
        });
    }

    /**
     * Mueve al personaje validando colisiones con muros y cajas.
     */
    private void moverPersonaje(String dir, int dx, int dy) {
        direccionMirando = dir;
        int nX = astronauta.getPosicionX() + dx;
        int nY = astronauta.getPosicionY() + dy;

        if (hayMuroEn(nX, nY)) {
            mensajeLog = "¡Camino bloqueado por un muro [M]!";
            return;
        }

        for (Utileria c : cajasMovibles) {
            if (c.getPosicionX() == nX && c.getPosicionY() == nY) {
                mensajeLog = "Caja [X] enfrente. Presiona [E] para empujarla.";
                return;
            }
        }

        astronauta.mover(dir, 1);
    }

    /**
     * Comprueba si existen obstáculos rígidos en las coordenadas dadas.
     */
    private boolean hayMuroEn(int x, int y) {
        for (Obstaculo m : muros) {
            if (m.getPosicionX() == x && m.getPosicionY() == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Cambia el ítem activo de la mochila al siguiente disponible.
     */
    private void cambiarObjetoEquipado() {
        if (mochila.getItems().isEmpty()) return;
        indiceObjetoEquipado = (indiceObjetoEquipado + 1) % mochila.getItems().size();
        Inventariable actual = mochila.getItems().get(indiceObjetoEquipado);
        mensajeLog = "Equipado: " + nombreItem(actual);
    }

    /**
     * Utiliza el ítem equipado en caso de ser un consumible.
     */
    private void usarObjetoEquipado() {
        if (mochila.getItems().isEmpty()) return;
        Inventariable item = mochila.getItems().get(indiceObjetoEquipado);

        if (item instanceof Recompensa) {
            Recompensa r = (Recompensa) item;
            astronauta.recibirDaño(-r.getValor());
            mochila.eliminarItem(r);
            indiceObjetoEquipado = 0;
            mensajeLog = "Consumido " + r.getNombre() + ". Vida restaurada +" + r.getValor();
        } else {
            mensajeLog = "El objeto equipado (" + nombreItem(item) + ") no es consumible.";
        }
    }

    /**
     * Genera un nuevo proyectil en la dirección donde mira el jugador.
     */
    private void dispararArma() {
        if (mochila.getItems().isEmpty()) return;
        Inventariable item = mochila.getItems().get(indiceObjetoEquipado);

        if (item instanceof Arma) {
            Arma arma = (Arma) item;
            int dx = 0, dy = 0;
            if (direccionMirando.equals("norte")) dy = -1;
            if (direccionMirando.equals("sur")) dy = 1;
            if (direccionMirando.equals("este")) dx = 1;
            if (direccionMirando.equals("oeste")) dx = -1;

            proyectiles.add(new Proyectil(astronauta.getPosicionX(), astronauta.getPosicionY(), dx, dy, (int)arma.getAlcance()));
            mensajeLog = "Disparo realizado con " + arma.getNombre();
        } else {
            mensajeLog = "¡Equipa el Cortador Plasma con [TAB] para poder disparar!";
        }
    }

    /**
     * Permite empujar cajas o iniciar la activación de interruptores.
     */
    private void interactuarInterruptorOCaja() {
        int fX = astronauta.getPosicionX();
        int fY = astronauta.getPosicionY();
        if (direccionMirando.equals("norte")) fY--;
        if (direccionMirando.equals("sur")) fY++;
        if (direccionMirando.equals("este")) fX++;
        if (direccionMirando.equals("oeste")) fX--;

        for (Utileria c : cajasMovibles) {
            if (c.getPosicionX() == fX && c.getPosicionY() == fY) {
                int destinoX = fX + (fX - astronauta.getPosicionX());
                int destinoY = fY + (fY - astronauta.getPosicionY());
                boolean libre = !hayMuroEn(destinoX, destinoY);

                for (Utileria otra : cajasMovibles) if (otra.getPosicionX() == destinoX && otra.getPosicionY() == destinoY) libre = false;

                if (libre) {
                    c.mover(direccionMirando, 1);
                    mensajeLog = "Empujaste " + c.getNombre();
                } else {
                    mensajeLog = "La caja no tiene espacio libre para moverse.";
                }
                return;
            }
        }

        for (Interruptor inter : interruptores) {
            if (inter.x == astronauta.getPosicionX() && inter.y == astronauta.getPosicionY() && !inter.activado && !inter.enProceso) {
                inter.enProceso = true;
                inter.tiempoInicioProceso = System.currentTimeMillis();
                mensajeLog = "¡Iniciando secuencia de 10s en interruptor [I]!";
            }
        }
    }

    /**
     * Arranca la ejecución continua del ciclo de actualización del juego.
     */
    private void iniciarGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            private long ultimoUpdate = 0;

            @Override
            public void handle(long now) {
                if (juegoTerminado) return;

                if (now - ultimoUpdate >= 250_000_000L) {
                    actualizarAliens();
                    actualizarProyectiles();
                    actualizarInterruptores();
                    verificarEstadoReglas();
                    renderizar();
                    ultimoUpdate = now;
                }
            }
        };
        timer.start();
    }

    /**
     * Actualiza la posición de los enemigos y verifica ataques al jugador.
     */
    private void actualizarAliens() {
        for (Alien alien : aliens) {
            int sigX = alien.x + alien.dx;
            boolean choca = hayMuroEn(sigX, alien.y);

            if (choca) {
                alien.dx *= -1;
            } else {
                alien.x = sigX;
            }

            if (alien.x == astronauta.getPosicionX() && alien.y == astronauta.getPosicionY()) {
                astronauta.recibirDaño(alien.daño);
                mensajeLog = "¡ALERTA! Un Alien [E] te atacó (-" + alien.daño + " HP)";
            }
        }
    }

    /**
     * Mueve los disparos y gestiona la eliminación de enemigos.
     */
    private void actualizarProyectiles() {
        Iterator<Proyectil> itP = proyectiles.iterator();
        while (itP.hasNext()) {
            Proyectil p = itP.next();
            p.x += p.dirX;
            p.y += p.dirY;
            p.alcanceRestante--;

            Iterator<Alien> itA = aliens.iterator();
            while (itA.hasNext()) {
                Alien a = itA.next();
                if (a.x == p.x && a.y == p.y) {
                    itA.remove();
                    itP.remove();
                    mensajeLog = "¡Alien [E] eliminado por tu disparo!";
                    return;
                }
            }

            if (p.alcanceRestante <= 0 || hayMuroEn(p.x, p.y)) itP.remove();
        }
    }

    /**
     * Procesa los tiempos de activación de las terminales.
     */
    private void actualizarInterruptores() {
        for (Interruptor inter : interruptores) {
            if (inter.enProceso && !inter.activado) {
                long transcurrido = (System.currentTimeMillis() - inter.tiempoInicioProceso) / 1000;
                inter.segundosRestantes = 10 - (int) transcurrido;
                if (inter.segundosRestantes <= 0) {
                    inter.activado = true;
                    inter.enProceso = false;

                    if (!checkPointIntermedio.isActivado()) {
                        checkPointIntermedio.activar();
                        mensajeLog = "¡Interruptor listo y CheckPoint [C1] activado! Progreso guardado.";
                    } else {
                        mensajeLog = "¡Interruptor [I] activado exitosamente!";
                    }
                }
            }
        }
    }

    /**
     * Evalúa las condiciones de derrota, reaparición y victoria.
     */
    private void verificarEstadoReglas() {
        if (astronauta.getVida() <= 0) {
            if (checkPointIntermedio.isActivado()) {
                astronauta.setPosicion(checkPointIntermedio.getPosicionX(), checkPointIntermedio.getPosicionY());
                int vidaFaltante = 100 - astronauta.getVida();
                astronauta.recibirDaño(-vidaFaltante);
                mensajeLog = "¡Soporte vital agotado! Reapareciendo en CheckPoint [C1]...";
            } else {
                juegoTerminado = true;
                victoria = false;
            }
        }

        boolean todosListos = true;
        for (Interruptor inter : interruptores) {
            if (!inter.activado) { todosListos = false; break; }
        }

        if (todosListos && !reactorSalida.isActivado()) {
            reactorSalida.activar();
            mensajeLog = "¡TODOS LOS INTERRUPTORES LISTOS! Dirígete a la Salida [C2].";
        }

        if (reactorSalida.isActivado()
                && astronauta.getPosicionX() == reactorSalida.getPosicionX()
                && astronauta.getPosicionY() == reactorSalida.getPosicionY()) {
            juegoTerminado = true;
            victoria = true;
        }
    }

    /**
     * Dibuja los elementos del juego y la interfaz en el lienzo.
     */
    private void renderizar() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.web("#0d1117"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.web("#21262d"));
        gc.setLineWidth(1);
        for (int x = 0; x < GRID_ANCHO; x++) {
            for (int y = 0; y < GRID_ALTO; y++) {
                gc.strokeRect(x * TAMANO_CELDA, y * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA);
            }
        }

        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 16));

        gc.setFill(Color.web("#30363d"));
        for (Obstaculo m : muros) {
            gc.fillRect(m.getPosicionX() * TAMANO_CELDA, m.getPosicionY() * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA);
            gc.setFill(Color.web("#8b949e"));
            gc.fillText("M", m.getPosicionX() * TAMANO_CELDA + 15, m.getPosicionY() * TAMANO_CELDA + 26);
            gc.setFill(Color.web("#30363d"));
        }

        for (Utileria c : cajasMovibles) {
            gc.setFill(Color.web("#d97706"));
            gc.fillRect(c.getPosicionX() * TAMANO_CELDA + 3, c.getPosicionY() * TAMANO_CELDA + 3, TAMANO_CELDA - 6, TAMANO_CELDA - 6);
            gc.setFill(Color.WHITE);
            gc.fillText("X", c.getPosicionX() * TAMANO_CELDA + 16, c.getPosicionY() * TAMANO_CELDA + 26);
        }

        for (Interruptor inter : interruptores) {
            if (inter.activado) gc.setFill(Color.LIME);
            else if (inter.enProceso) gc.setFill(Color.ORANGE);
            else gc.setFill(Color.RED);

            gc.fillRect(inter.x * TAMANO_CELDA + 6, inter.y * TAMANO_CELDA + 6, TAMANO_CELDA - 12, TAMANO_CELDA - 12);

            gc.setFill(Color.BLACK);
            if (inter.enProceso) {
                gc.setFont(Font.font("Consolas", FontWeight.BOLD, 12));
                gc.fillText(inter.segundosRestantes + "s", inter.x * TAMANO_CELDA + 12, inter.y * TAMANO_CELDA + 26);
                gc.setFont(Font.font("Consolas", FontWeight.BOLD, 16));
            } else {
                gc.fillText("I", inter.x * TAMANO_CELDA + 16, inter.y * TAMANO_CELDA + 27);
            }
        }

        if (checkPointIntermedio.isActivado()) {
            gc.setFill(Color.web("#22c55e"));
        } else {
            gc.setFill(Color.web("#4b5563"));
        }
        gc.fillRect(checkPointIntermedio.getPosicionX() * TAMANO_CELDA + 4, checkPointIntermedio.getPosicionY() * TAMANO_CELDA + 4, TAMANO_CELDA - 8, TAMANO_CELDA - 8);
        gc.setFill(Color.BLACK);
        gc.fillText("C1", checkPointIntermedio.getPosicionX() * TAMANO_CELDA + 12, checkPointIntermedio.getPosicionY() * TAMANO_CELDA + 27);

        gc.setFill(reactorSalida.isActivado() ? Color.web("#00e5ff") : Color.web("#4b5563"));
        gc.fillRect(reactorSalida.getPosicionX() * TAMANO_CELDA + 4, reactorSalida.getPosicionY() * TAMANO_CELDA + 4, TAMANO_CELDA - 8, TAMANO_CELDA - 8);
        gc.setFill(Color.BLACK);
        gc.fillText("C2", reactorSalida.getPosicionX() * TAMANO_CELDA + 12, reactorSalida.getPosicionY() * TAMANO_CELDA + 27);

        for (Alien a : aliens) {
            gc.setFill(Color.web("#a855f7"));
            gc.fillOval(a.x * TAMANO_CELDA + 4, a.y * TAMANO_CELDA + 4, TAMANO_CELDA - 8, TAMANO_CELDA - 8);
            gc.setFill(Color.WHITE);
            gc.fillText("E", a.x * TAMANO_CELDA + 16, a.y * TAMANO_CELDA + 27);
        }

        gc.setFill(Color.YELLOW);
        for (Proyectil p : proyectiles) {
            gc.fillOval(p.x * TAMANO_CELDA + 16, p.y * TAMANO_CELDA + 16, 10, 10);
        }

        int px = astronauta.getPosicionX() * TAMANO_CELDA;
        int py = astronauta.getPosicionY() * TAMANO_CELDA;
        gc.setFill(Color.web("#10b981"));
        gc.fillOval(px + 4, py + 4, TAMANO_CELDA - 8, TAMANO_CELDA - 8);

        gc.setFill(Color.WHITE);
        if (direccionMirando.equals("norte")) gc.fillOval(px + 17, py + 6, 8, 8);
        if (direccionMirando.equals("sur")) gc.fillOval(px + 17, py + 28, 8, 8);
        if (direccionMirando.equals("este")) gc.fillOval(px + 28, py + 17, 8, 8);
        if (direccionMirando.equals("oeste")) gc.fillOval(px + 6, py + 17, 8, 8);

        barVida.setProgress(Math.max(0, astronauta.getVida() / 100.0));
        lblInfoJugador.setText(String.format("JUGADOR: %s | MIRA: %s | EQUIPADO: %s",
                astronauta.getNombre(), direccionMirando.toUpperCase(),
                mochila.getItems().isEmpty() ? "Ninguno" : nombreItem(mochila.getItems().get(indiceObjetoEquipado))));

        lblMochila.setText("MOCHILA ([TAB]: Cambiar, [R]: Consumir, [ESPACIO]: Disparar): " + obtenerListaMochilaFormateada());
        lblLog.setText("LOG DE NAVE: " + mensajeLog);

        if (juegoTerminado) {
            gc.setFill(Color.rgb(0, 0, 0, 0.85));
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            gc.setFont(Font.font("Consolas", FontWeight.BOLD, 28));
            if (victoria) {
                gc.setFill(Color.LIME);
                gc.fillText("¡MISIÓN CUMPLIDA!", 180, 300);
            } else {
                gc.setFill(Color.RED);
                gc.fillText("¡MORTALMENTE DERROTADO!", 130, 300);
            }
        }
    }

    /**
     * Obtiene el nombre identificador de un objeto del inventario.
     */
    private String nombreItem(Inventariable item) {
        if (item instanceof Arma) {
            return ((Arma) item).getNombre();
        } else if (item instanceof Recompensa) {
            return ((Recompensa) item).getNombre();
        }
        return item.getClass().getSimpleName();
    }

    /**
     * Formatea el texto explicativo con los elementos de la mochila.
     */
    private String obtenerListaMochilaFormateada() {
        if (mochila.getItems().isEmpty()) return "(Vacía)";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mochila.getItems().size(); i++) {
            Inventariable item = mochila.getItems().get(i);
            if (i == indiceObjetoEquipado) sb.append(" [-> ").append(nombreItem(item)).append(" <-] ");
            else sb.append(" ").append(nombreItem(item)).append(" ");
        }
        return sb.toString();
    }

    /**
     * Método ejecutor principal.
     */
    public static void main(String[] args) {
        launch(args);
    }
}