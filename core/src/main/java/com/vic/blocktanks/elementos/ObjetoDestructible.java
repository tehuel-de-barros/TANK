package com.vic.blocktanks.elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.vic.blocktanks.utilidades.Globales;
import com.badlogic.gdx.math.Rectangle;

public class ObjetoDestructible {

    private Sprite sprite;
    private int lives;  // Barril necesita 2 disparos para destruirse
    private Rectangle bounds;

    public ObjetoDestructible(float x, float y) {
        this.lives = 2; // Barril se destruye con 2 disparos
        sprite = new Sprite(new Texture("barril.png")); // Imagen del barril
        sprite.setSize(1f, 1f); // Barril ocupa solo un cuadrado de 1x1
        sprite.setPosition(x, y);
        bounds = new Rectangle(x, y, 1f, 1f); // Cambiado a solo 1 cuadrado de tamaño
    }

    public void draw() {
        sprite.draw(Globales.batch);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void decreaseLife() {
        this.lives--;
    }

    public int getLives() {
        return lives;
    }

    public boolean isDestroyed() {
        return lives <= 0; // Barril destruido
    }
}
