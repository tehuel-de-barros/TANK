package com.vic.blocktanks.elementos;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.List;
import java.util.Random;

public class BotTank extends Tank {
    public enum ModoBot {
        AGRESIVO,
        ORBITANTE
    }

    private ModoBot modo;
    private float timerDisparo = 0f;
    private float intervaloDisparo = 0.7f; // Intervalo de disparo (ajustable)
    private Random random;

    // Variables para el modo ORBITANTE (patrulla aleatoria)
    private float patrolTimer = 0f;
    private float patrolInterval = 2f; // Cada 2 segundos, cambia de dirección
    private Vector2 patrolDirection;

    public BotTank(float ancho, float alto, float x, float y, ModoBot modo) {
        super(ancho, alto, x, y);
        this.modo = modo;
        random = new Random();
        if (modo == ModoBot.ORBITANTE) {
            patrolDirection = getRandomDirection();
        }
    }

    private Vector2 getRandomDirection() {
        float ang = random.nextFloat() * 360f;
        return new Vector2((float)Math.cos(Math.toRadians(ang)), (float)Math.sin(Math.toRadians(ang)));
    }

    /**
     * Actualiza el bot.
     * En modo AGRESIVO: se mueve hacia el jugador (con leve oscilación) y dispara;
     * se verifica colisión con obstáculos, con otros bots y con el jugador.
     * En modo ORBITANTE: patrulla aleatoriamente; si se acerca el jugador (<5 unidades), cambia a agresivo.
     *
     * @param delta Tiempo transcurrido.
     * @param obstacles Lista de obstáculos.
     * @param worldWidth Ancho del mundo.
     * @param worldHeight Alto del mundo.
     * @param playerPos Centro del jugador (Vector2).
     * @param allBots Lista de todos los bots.
     * @param jugador Objeto Tank del jugador (para colisión).
     */
    public void update(float delta, List<Rectangle> obstacles, float worldWidth, float worldHeight,
                       Vector2 playerPos, List<BotTank> allBots, Tank jugador) {
        if (modo == ModoBot.AGRESIVO) {
            float mySpeed = speed * 0.8f; // Velocidad reducida
            Vector2 centro = new Vector2(x + ancho/2, y + alto/2);
            Vector2 dir = playerPos.cpy().sub(centro);
            if (dir.len() > 0) {
                dir.nor();
                // Leve oscilación para evitar movimiento 100% recto
                float offset = (float)Math.sin(timerDisparo * 3) * 0.1f;
                float moveAngle = dir.angleDeg() + offset;
                Vector2 moveDir = new Vector2((float)Math.cos(Math.toRadians(moveAngle)), (float)Math.sin(Math.toRadians(moveAngle)));
                float proposedX = x + moveDir.x * mySpeed * delta;
                float proposedY = y + moveDir.y * mySpeed * delta;
                Rectangle proposedBounds = new Rectangle(proposedX, proposedY, ancho, alto);
                boolean collides = false;
                // Colisión con obstáculos
                for (Rectangle rect : obstacles) {
                    if (proposedBounds.overlaps(rect)) {
                        collides = true;
                        break;
                    }
                }
                // Colisión con otros bots
                for (BotTank bot : allBots) {
                    if (bot != this && proposedBounds.overlaps(bot.getBounds())) {
                        collides = true;
                        break;
                    }
                }
                // Colisión con el jugador
                if (proposedBounds.overlaps(jugador.getBounds())) {
                    collides = true;
                }
                if (!collides) {
                    x = proposedX;
                    y = proposedY;
                }
                angle = moveAngle;
            }
            timerDisparo += delta;
            if (timerDisparo >= intervaloDisparo) {
                disparar();
                timerDisparo = 0f;
            }
        } else if (modo == ModoBot.ORBITANTE) {
            patrolTimer += delta;
            if (patrolTimer >= patrolInterval) {
                patrolDirection = getRandomDirection();
                patrolTimer = 0f;
            }
            float proposedX = x + patrolDirection.x * speed * delta;
            float proposedY = y + patrolDirection.y * speed * delta;
            Rectangle proposedBounds = new Rectangle(proposedX, proposedY, ancho, alto);
            boolean collides = false;
            for (Rectangle rect : obstacles) {
                if (proposedBounds.overlaps(rect)) { collides = true; break; }
            }
            for (BotTank bot : allBots) {
                if (bot != this && proposedBounds.overlaps(bot.getBounds())) { collides = true; break; }
            }
            // También, evitar superposición con el jugador
            if (proposedBounds.overlaps(jugador.getBounds())) {
                collides = true;
            }
            if (!collides) {
                x = proposedX;
                y = proposedY;
            }
            angle = patrolDirection.angleDeg();
            // Si el jugador se acerca, cambia a agresivo
            if (new Vector2(x + ancho/2, y + alto/2).dst(playerPos) < 5f) {
                modo = ModoBot.AGRESIVO;
                timerDisparo = 0f;
            }
        }
        // Limitar posición en el mundo
        x = Math.max(0, Math.min(x, worldWidth - ancho));
        y = Math.max(0, Math.min(y, worldHeight - alto));
        spr.setPosition(x, y);
        spr.setRotation(angle - 90);
        // Actualizar balas
        for (Bala b : balas) {
            b.update(delta, obstacles);
        }
        balas.removeIf(b -> !b.isActive());
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, ancho, alto);
    }
}
