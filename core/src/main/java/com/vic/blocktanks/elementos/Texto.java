package com.vic.blocktanks.elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.vic.blocktanks.utilidades.Globales;

public class Texto {

    BitmapFont fuente;
    private float x = 0, y = 0;
    private String texto = "";
    GlyphLayout layout;

    public Texto(String rutaFuente, int dimension, Color color, boolean sombra ) {
        generarTexto(rutaFuente, dimension, color, sombra);
    }

    private void generarTexto(String rutaFuente, int dimension, Color color, boolean sombra) {
        FreeTypeFontGenerator generador = new FreeTypeFontGenerator(Gdx.files.internal(rutaFuente));
        FreeTypeFontGenerator.FreeTypeFontParameter parametros = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parametros.size = dimension;
        parametros.color = color;
        if(sombra) {
            parametros.shadowColor = Color.BLACK;
            parametros.shadowOffsetX = 1;
            parametros.shadowOffsetY = 1;
        }
        fuente = generador.generateFont(parametros);
        layout = new GlyphLayout();
    }

    public void dibujar() {
        fuente.draw(Globales.batch, texto, x, y);
    }

    public void setColor(Color color) {
        fuente.setColor(color);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
        layout.setText(fuente, texto);
    }

    public float getAncho() {
        return layout.width;
    }

    public float getAlto() {
        return layout.height;
    }

    public Vector2 getDimension() {
        return new Vector2(layout.width, layout.height);
    }

    public Vector2 getPosicion() {
        return new Vector2(x, y);
    }

}
