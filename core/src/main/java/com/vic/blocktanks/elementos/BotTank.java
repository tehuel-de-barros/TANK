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
    private float intervaloDisparo = 0.7f;
    private Random random;

    // Variables para modo ORBITANTE (patrulla aleatoria)
    private float patrolTimer = 0f;
    private float patrolInterval = 2f;
    private Vector2 patrolDirection;

    public BotTank(float ancho, float alto, float x, float y, ModoBot modo) {
        super(ancho, alto, x, y);
        this.modo = modo;
        random = new Random();
        if(modo == ModoBot.ORBITANTE) {
            patrolDirection = getRandomDirection();
        }
    }

    private Vector2 getRandomDirection() {
        float ang = random.nextFloat() * 360f;
        return new Vector2((float)Math.cos(Math.toRadians(ang)), (float)Math.sin(Math.toRadians(ang)));
    }

    @Override
    public void disparar() {
        float centroX = x + ancho/2;
        float centroY = y + alto/2;
        float largoCanon = alto * 0.6f;
        float balaX = centroX + (float)Math.cos(Math.toRadians(angle)) * largoCanon;
        float balaY = centroY + (float)Math.sin(Math.toRadians(angle)) * largoCanon;
        // Usamos el constructor original de Bala (3 parámetros)
        balas.add(new Bala(balaX, balaY, angle));
    }

    /**
     * Actualiza el bot comprobando colisiones.
     * En modo AGRESIVO: se mueve hacia el jugador; si el movimiento propuesto genera colisión
     * con obstáculos, otros bots o el jugador, se aplica un empuje suave hacia atrás (con velocidad de rebote).
     * En modo ORBITANTE: patrulla aleatoriamente; si hay colisión, se aplica un empuje suave en dirección contraria;
     * si el jugador se acerca (<5 unidades), cambia a agresivo.
     */
    public void update(float delta, List<Rectangle> obstacles, float worldWidth, float worldHeight,
                       Vector2 playerPos, List<BotTank> allBots, Tank jugador) {
        if(modo == ModoBot.AGRESIVO) {
            float mySpeed = speed * 0.8f;
            Vector2 centro = new Vector2(x + ancho/2, y + alto/2);
            Vector2 dir = playerPos.cpy().sub(centro);
            if(dir.len() > 0) {
                dir.nor();
                float offset = (float)Math.sin(timerDisparo * 3) * 0.1f;
                float moveAngle = dir.angleDeg() + offset;
                Vector2 moveDir = new Vector2((float)Math.cos(Math.toRadians(moveAngle)),
                    (float)Math.sin(Math.toRadians(moveAngle)));
                float proposedX = x + moveDir.x * mySpeed * delta;
                float proposedY = y + moveDir.y * mySpeed * delta;
                Rectangle proposedBounds = new Rectangle(proposedX, proposedY, ancho, alto);
                boolean collides = false;
                // Verificar colisión con obstáculos
                for (Rectangle rect : obstacles) {
                    if(proposedBounds.overlaps(rect)) {
                        collides = true;
                        break;
                    }
                }
                // Verificar colisión con otros bots
                for(BotTank bot : allBots) {
                    if(bot != this && proposedBounds.overlaps(bot.getBounds())) {
                        collides = true;
                        break;
                    }
                }
                // Verificar colisión con el jugador
                if(proposedBounds.overlaps(jugador.getBounds())) {
                    collides = true;
                }
                if(!collides) {
                    x = proposedX;
                    y = proposedY;
                } else {
                    // Rebote suave: empuje hacia atrás con velocidad de rebote
                    float bounceSpeed = 3f; // unidades/segundo
                    x -= moveDir.x * bounceSpeed * delta;
                    y -= moveDir.y * bounceSpeed * delta;
                }
                angle = moveAngle;
            }
            timerDisparo += delta;
            if(timerDisparo >= intervaloDisparo) {
                disparar();
                timerDisparo = 0f;
            }
        } else if(modo == ModoBot.ORBITANTE) {
            patrolTimer += delta;
            if(patrolTimer >= patrolInterval) {
                patrolDirection = getRandomDirection();
                patrolTimer = 0f;
            }
            float proposedX = x + patrolDirection.x * speed * delta;
            float proposedY = y + patrolDirection.y * speed * delta;
            Rectangle proposedBounds = new Rectangle(proposedX, proposedY, ancho, alto);
            boolean collides = false;
            for (Rectangle rect : obstacles) {
                if(proposedBounds.overlaps(rect)) { collides = true; break; }
            }
            for(BotTank bot : allBots) {
                if(bot != this && proposedBounds.overlaps(bot.getBounds())) { collides = true; break; }
            }
            if(proposedBounds.overlaps(jugador.getBounds())) { collides = true; }
            if(!collides) {
                x = proposedX;
                y = proposedY;
            } else {
                // Rebote suave en modo orbitante: empuje hacia atrás
                float bounceSpeed = 3f;
                x -= patrolDirection.x * bounceSpeed * delta;
                y -= patrolDirection.y * bounceSpeed * delta;
            }
            angle = patrolDirection.angleDeg();
            Vector2 centro = new Vector2(x + ancho/2, y + alto/2);
            if(centro.dst(playerPos) < 5f) {
                modo = ModoBot.AGRESIVO;
                timerDisparo = 0f;
            }
        }
        // Limitar la posición dentro del mundo
        x = Math.max(0, Math.min(x, worldWidth - ancho));
        y = Math.max(0, Math.min(y, worldHeight - alto));
        spr.setPosition(x, y);
        spr.setRotation(angle - 90);
        for(Bala b : balas) {
            b.update(delta, obstacles);
        }
        balas.removeIf(b -> !b.isActive());
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, ancho, alto);
    }
}
