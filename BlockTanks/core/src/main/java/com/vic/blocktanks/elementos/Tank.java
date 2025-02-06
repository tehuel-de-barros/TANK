package com.vic.blocktanks.elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.vic.blocktanks.utilidades.Globales;

public class Tank {
    public Texture textura;
    public Sprite spr;
    public float alto,ancho;
    public float x,y;

    public Tank(float ancho, float alto, float x, float y ) {
        textura = new Texture("tank.png");
        spr = new Sprite(textura);
        this.x = x;
        this.y = y;
        this.alto = alto;
        this.ancho = ancho;
        spr.setSize(ancho, alto);
        spr.setPosition(x, y);

    }

    public void dibujar() {
        spr.draw(Globales.batch);
    }
}
