# Supervivencia Espacial

Proyecto de videojuego 2D interactivo desarrollado en JavaFX, basado en una arquitectura de Programación Orientada a Objetos (POO) y el patrón de diseño estipulado en el diagrama de clases UML del curso.

---

## Descripción del Proyecto

El jugador asume el rol de un astronauta atrapado en una nave espacial en crisis. El objetivo es explorar un laberinto de 15x15 casillas, gestionar los ítems de la mochila, esquivar o eliminar enemigos en movimiento (Aliens), empujar obstáculos para abrir rutas y activar los interruptores con temporizador para desbloquear la salida de emergencia.

---

## Características Principales

- **Arquitectura Modular**: Separación de responsabilidades en la clase principal `JuegoFX` (inicialización, controles, lógica del loop de juego y renderizado).
- **Tablero Dinámico**: Matriz de 15x15 casillas con detección de colisiones exacta.
- **Ciclo de Juego (Game Loop)**: Animación constante a través de `AnimationTimer` para el movimiento de enemigos, proyectiles y conteos en tiempo real.
- **Interfaz de Usuario (HUD)**: Panel inferior con barra de soporte vital (`ProgressBar`), indicador de dirección, objeto equipado, inventario interactivo y registro de eventos (Log).
- **Mecánica de CheckPoint / Respawn**: Activación de punto de reaparición al completar el hackeo de una terminal, evitando la pérdida total de progreso al morir.

---

## Controles del Juego

| Tecla | Acción |
| :--- | :--- |
| **W, A, S, D** / **Flechas** | Mover al personaje y orientar la dirección de mira |
| **TAB** / **Q** | Alternar entre los objetos guardados en la mochila |
| **R** | Consumir / Usar el objeto actualmente equipado (ej. Botiquín, Poción O2) |
| **ESPACIO** | Disparar el arma equipada (Cortador Plasma) hacia la dirección de mira |
| **E** | Empujar cajas frontales / Iniciar secuencia de hackeo en interruptores |

---

## Leyenda y Simbología del Tablero

- **`[M]` Muro**: Obstáculo rígido que bloquea el paso.
- **`[X]` Caja (Utileria)**: Elemento móvil que puede ser empujado con la tecla `E` si la casilla posterior está libre.
- **`[I]` Interruptor**: Terminal que requiere permanecer sobre la casilla e iniciar una cuenta regresiva de 10 segundos.
- **`[E]` Alien / Enemigo**: Entidad dinámica que patrulla el área y resta vida al contacto.
- **`[C1]` CheckPoint Respawn**: Punto de guardado intermedio. Se activa al completar un interruptor; si la vida del jugador llega a cero, reaparecerá en esta posición con la salud restaurada.
- **`[C2]` CheckPoint Salida**: Portal de escape final. Se desbloquea únicamente cuando todos los interruptores `[I]` del mapa han sido activados.

---

## Estructura del Código (Modelos UML)

El proyecto vincula los componentes de la interfaz gráfica con el backend de clases del modelo:

- **`Personaje`**: Modela los atributos del jugador (vida, nombre, coordenadas) e implementa las interfaces `ElementoDinamico` y `Destruible`.
- **`Nivel`**: Contenedor agregador que gestiona la lista de obstáculos, elementos dinámicos, checkpoints e inventario.
- **`CheckPoint`**: Controla el estado activo/inactivo de las terminales de guardado y salida.
- **`Inventario` / `Inventariable`**: Sistema polimórfico para almacenar `Arma` (ofensiva) y `Recompensa` (consumibles de curación).
- **`Obstaculo` y `Utileria`**: Elementos de mapa inmóviles o desplazables.
- **`JuegoFX`**: Clase principal que extiende de `Application` (JavaFX), responsable del pipeline de renderizado en `Canvas` y la captura de eventos del sistema.

---

## Requisitos de Ejecución

1. **Java JDK**: Versión 11 o superior.
2. **JavaFX SDK**: Módulos `javafx.controls` y `javafx.graphics`.

---