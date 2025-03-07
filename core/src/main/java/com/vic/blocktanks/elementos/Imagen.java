package com.vic.blocktanks.elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.vic.blocktanks.utilidades.Globales;

public class Imagen {
    private Texture t;
    private Sprite s;

    public Imagen(String r) {
        t = new Texture(r);
        // Aplicar filtrado lineal para suavizar la imagen al escalarla
        t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        s = new Sprite(t);
    }

    public void dibujar() {
        s.draw(Globales.batch);
    }

    public void setTransparencia(float a) {
        s.setAlpha(a);
    }

    public void setSize(float ancho, float alto) {
        s.setSize(ancho, alto);
    }
}
