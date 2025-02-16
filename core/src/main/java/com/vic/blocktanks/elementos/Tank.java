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
    public float x, y;
    public float ancho, alto;
    public float speed = 3f;
    public float angle = 0;

    // Cambié estas variables a protected para que BotTank pueda usarlas
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
        float centroX = x + ancho / 2f;
        float centroY = y + alto / 2f;
        // Ajustamos el largo del cañón para acercar la bala al tanque.
        float largoCanon = alto * 0.6f;  // Antes 0.8, ahora más cerca (0.6)
        float balaX = centroX + (float)Math.cos(Math.toRadians(angle)) * largoCanon;
        float balaY = centroY + (float)Math.sin(Math.toRadians(angle)) * largoCanon;
        balas.add(new Bala(balaX, balaY, angle));
        System.out.println("DISPARO → Bala en X: " + balaX + ", Y: " + balaY + " | Ángulo: " + angle);
    }



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

    public void dibujar() {
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
}
