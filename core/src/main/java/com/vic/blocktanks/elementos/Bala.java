package com.vic.blocktanks.elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.vic.blocktanks.utilidades.Config;
import com.vic.blocktanks.utilidades.Globales;
import java.util.List;

public class Bala {
    private float x, y;               // Posición de la bala en unidades
    private float speed = 10f;        // Velocidad en unidades/segundo
    private float angle;              // Ángulo de disparo en grados
    private Sprite sprite;            // Sprite de la bala
    private boolean active = true;    // Estado activo
    private Rectangle bounds;         // Rectángulo para colisión

    // Margen para reducir el área de colisión
    private float collisionMargin = 0.6f;

    public Bala(float x, float y, float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;

        Texture textura = new Texture("bala.png");
        sprite = new Sprite(textura);
        sprite.setSize(2f, 2f);
        sprite.setOriginCenter();
        sprite.setPosition(x - sprite.getWidth()/2, y - sprite.getHeight()/2);
        sprite.setRotation(angle - 90);

        float colWidth = sprite.getWidth() - 2 * collisionMargin;
        float colHeight = sprite.getHeight() - 2 * collisionMargin;
        bounds = new Rectangle(
            x - sprite.getWidth()/2 + collisionMargin,
            y - sprite.getHeight()/2 + collisionMargin,
            colWidth,
            colHeight);
    }

    public void update(float delta, List<Rectangle> obstacles) {
        x += Math.cos(Math.toRadians(angle)) * speed * delta;
        y += Math.sin(Math.toRadians(angle)) * speed * delta;

        sprite.setPosition(x - sprite.getWidth()/2, y - sprite.getHeight()/2);
        sprite.setRotation(angle - 90);
        bounds.setPosition(x - sprite.getWidth()/2 + collisionMargin, y - sprite.getHeight()/2 + collisionMargin);

        float worldWidth = Config.ANCHO / 32f;
        float worldHeight = Config.ALTO / 32f;
        if (x < 0 || x > worldWidth || y < 0 || y > worldHeight) {
            active = false;
        }
        for (Rectangle obstacle : obstacles) {
            if (bounds.overlaps(obstacle)) {
                active = false;
                break;
            }
        }
    }

    public void draw() {
        if (active) {
            sprite.draw(Globales.batch);
        }
    }

    public boolean isActive() {
        return active;
    }
}
