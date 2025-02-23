package com.vic.blocktanks.elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.vic.blocktanks.utilidades.Globales;
import java.util.List;

public class Player2 extends Tank {
    public Player2(float ancho, float alto, float x, float y) {
        super(ancho, alto, x, y);
    }

    @Override
    public void update(float delta, List<Rectangle> obstacles, float worldWidth, float worldHeight) {
        // Usar WASD para mover a Player2
        Vector2 input = new Vector2();
        if (Gdx.input.isKeyPressed(Input.Keys.A)) input.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) input.x += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) input.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) input.y -= 1;
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
        // Disparo con la tecla F
        if(Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            disparar();
        }
        balas.removeIf(b -> !b.isActive());
        for(Bala b : balas) {
            b.update(delta, obstacles);
        }
    }
}
