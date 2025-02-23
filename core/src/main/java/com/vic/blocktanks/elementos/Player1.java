package com.vic.blocktanks.elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.vic.blocktanks.utilidades.Globales;
import java.util.List;

public class Player1 extends Tank {
    public Player1(float ancho, float alto, float x, float y) {
        super(ancho, alto, x, y);
    }

    @Override
    public void update(float delta, List<Rectangle> obstacles, float worldWidth, float worldHeight) {
        // Usar flechas para mover a Player1
        Vector2 input = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  input.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) input.x += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.UP))    input.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))  input.y -= 1;
        if(input.len() > 0) {
            angle = input.angleDeg();
        }
        float newX = x + input.x * speed * delta;
        float newY = y + input.y * speed * delta;
        newX = Math.max(0, Math.min(newX, worldWidth - ancho));
        newY = Math.max(0, Math.min(newY, worldHeight - alto));
        Rectangle newBounds = new Rectangle(newX, newY, ancho, alto);
        boolean collision = false;
        for (Rectangle rect : obstacles) {
            if(newBounds.overlaps(rect)) {
                collision = true;
                break;
            }
        }
        if(!collision) {
            x = newX;
            y = newY;
        }
        if(input.len() > 0) {
            angle = input.angleDeg();
        }
        spr.setPosition(x, y);
        spr.setRotation(angle - 90);
        // Disparo con la barra espaciadora
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            disparar();
        }
        balas.removeIf(b -> !b.isActive());
        for(Bala b : balas) {
            b.update(delta, obstacles);
        }
    }
}
