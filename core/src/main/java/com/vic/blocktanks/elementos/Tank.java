package com.vic.blocktanks.elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.vic.blocktanks.utilidades.Globales;
import java.util.ArrayList;
import java.util.List;

public class Tank {
    public float x, y;         // Posición
    public float ancho, alto;  // Dimensiones
    public float speed = 3f;   // Velocidad (jugador)
    public float angle = 0;    // Ángulo en grados

    // Vidas del tanque (por defecto 3)
    protected int lives = 3;

    protected Texture textura;
    protected Sprite spr;
    protected List<Bala> balas = new ArrayList<>();

    public Tank(float ancho, float alto, float x, float y) {
        this.ancho = ancho;
        this.alto = alto;
        this.x = x;
        this.y = y;
        textura = new Texture("tank2.png");
        spr = new Sprite(textura);
        spr.setSize(ancho, alto);
        spr.setOriginCenter();
        spr.setPosition(x, y);
    }

    public void disparar() {
        float centroX = x + ancho / 2;
        float centroY = y + alto / 2;
        float largoCanon = alto * 0.6f;
        float balaX = centroX + (float) Math.cos(Math.toRadians(angle)) * largoCanon;
        float balaY = centroY + (float) Math.sin(Math.toRadians(angle)) * largoCanon;
        balas.add(new Bala(balaX, balaY, angle));
        System.out.println("DISPARO → Bala en X: " + balaX + ", Y: " + balaY + " | Ángulo: " + angle);
    }

    // Actualiza el jugador según el input
    public void update(float delta, List<Rectangle> obstacles, float worldWidth, float worldHeight) {
        Vector2 input = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.A)) input.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) input.x += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) input.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) input.y -= 1;
        if (input.len() > 0) {
            angle = input.angleDeg();
        }
        float newX = x + input.x * speed * delta;
        float newY = y + input.y * speed * delta;
        newX = Math.max(0, Math.min(newX, worldWidth - ancho));
        newY = Math.max(0, Math.min(newY, worldHeight - alto));
        Rectangle newBounds = new Rectangle(newX, newY, ancho, alto);
        boolean collision = false;
        for (Rectangle rect : obstacles) {
            if (newBounds.overlaps(rect)) {
                collision = true;
                break;
            }
        }
        if (!collision) {
            x = newX;
            y = newY;
        }
        if (input.len() > 0) {
            angle = input.angleDeg();
        }
        spr.setPosition(x, y);
        spr.setRotation(angle - 90);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            disparar();
        }
        balas.removeIf(b -> !b.isActive());
        for (Bala b : balas) {
            b.update(delta, obstacles);
        }
    }

    public void draw() {
        spr.draw(Globales.batch);
        for (Bala b : balas) {
            b.draw();
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, ancho, alto);
    }

    public void dispose() {
        textura.dispose();
    }

    public void updateSprite() {
        spr.setPosition(x, y);
        spr.setRotation(angle - 90);
    }

    // Métodos para manejar las vidas
    public void reduceLife() {
        lives--;
    }

    public int getLives() {
        return lives;
    }

    // Setter para asignar la cantidad de vidas
    public void setLives(int lives) {
        this.lives = lives;
    }

    public List<Bala> getBalas() {
        return balas;
    }
}
