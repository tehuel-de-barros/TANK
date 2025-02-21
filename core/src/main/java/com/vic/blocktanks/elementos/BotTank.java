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

    // Variables para la animación de desliz
    private boolean sliding = false;
    private float slideTimer = 0f;
    private final float SLIDE_DURATION = 0.5f;
    private Vector2 slideDirection = new Vector2();
    private float slideSpeed = 2f;

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

    // Método auxiliar para verificar colisiones con obstáculos, otros bots y el jugador
    private boolean isColliding(List<Rectangle> obstacles, List<BotTank> allBots, Tank jugador) {
        Rectangle myBounds = getBounds();
        for (Rectangle rect : obstacles) {
            if(myBounds.overlaps(rect)) return true;
        }
        for(BotTank bot : allBots) {
            if(bot != this && myBounds.overlaps(bot.getBounds())) return true;
        }
        if(myBounds.overlaps(jugador.getBounds())) return true;
        return false;
    }

    // Método auxiliar para mover en subpasos y detenerse justo antes de colisionar
    private void moveWithSteps(float dx, float dy,
                               List<Rectangle> obstacles,
                               float worldWidth, float worldHeight,
                               List<BotTank> allBots,
                               Tank jugador) {
        int steps = 5;  // Ajusta el número de subpasos si es necesario
        float stepX = dx / steps;
        float stepY = dy / steps;
        for (int i = 0; i < steps; i++) {
            x += stepX;
            y += stepY;
            x = Math.max(0, Math.min(x, worldWidth - ancho));
            y = Math.max(0, Math.min(y, worldHeight - alto));
            if(isColliding(obstacles, allBots, jugador)) {
                x -= stepX;
                y -= stepY;
                break;
            }
        }
    }

    @Override
    public void disparar() {
        float centroX = x + ancho/2;
        float centroY = y + alto/2;
        float largoCanon = alto * 0.6f;
        float balaX = centroX + (float)Math.cos(Math.toRadians(angle)) * largoCanon;
        float balaY = centroY + (float)Math.sin(Math.toRadians(angle)) * largoCanon;
        balas.add(new Bala(balaX, balaY, angle));
    }

    public void update(float delta, List<Rectangle> obstacles, float worldWidth, float worldHeight,
                       Vector2 playerPos, List<BotTank> allBots, Tank jugador) {
        // Si el bot está en estado de desliz, se mueve en subpasos
        if(sliding) {
            float dx = slideDirection.x * slideSpeed * delta;
            float dy = slideDirection.y * slideSpeed * delta;
            moveWithSteps(dx, dy, obstacles, worldWidth, worldHeight, allBots, jugador);
            slideTimer -= delta;
            spr.setPosition(x, y);
            angle = slideDirection.angleDeg();
            spr.setRotation(angle - 90);
            for(Bala b : balas) {
                b.update(delta, obstacles);
            }
            balas.removeIf(b -> !b.isActive());
            if(slideTimer <= 0) {
                sliding = false;
                if(modo == ModoBot.ORBITANTE) {
                    patrolDirection = getRandomDirection();
                }
            }
            return;
        }

        // Movimiento normal según el modo
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
                for (Rectangle rect : obstacles) {
                    if(proposedBounds.overlaps(rect)) {
                        collides = true;
                        break;
                    }
                }
                for(BotTank bot : allBots) {
                    if(bot != this && proposedBounds.overlaps(bot.getBounds())) {
                        collides = true;
                        break;
                    }
                }
                if(proposedBounds.overlaps(jugador.getBounds())) {
                    collides = true;
                }
                if(!collides) {
                    float dx = proposedX - x;
                    float dy = proposedY - y;
                    moveWithSteps(dx, dy, obstacles, worldWidth, worldHeight, allBots, jugador);
                } else {
                    // Inicia animación de desliz (giro 180° = retroceso)
                    sliding = true;
                    slideTimer = SLIDE_DURATION;
                    slideDirection = moveDir.cpy();
                    slideDirection.scl(-1);
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
                float dx = proposedX - x;
                float dy = proposedY - y;
                moveWithSteps(dx, dy, obstacles, worldWidth, worldHeight, allBots, jugador);
            } else {
                sliding = true;
                slideTimer = SLIDE_DURATION;
                slideDirection = patrolDirection.cpy();
                slideDirection.scl(-1);
            }
            angle = patrolDirection.angleDeg();
            Vector2 centro = new Vector2(x + ancho/2, y + alto/2);
            if(centro.dst(playerPos) < 5f) {
                modo = ModoBot.AGRESIVO;
                timerDisparo = 0f;
            }
        }
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
