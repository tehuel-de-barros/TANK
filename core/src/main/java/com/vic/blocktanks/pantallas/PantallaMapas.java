package com.vic.blocktanks.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.vic.blocktanks.elementos.Texto;
import com.vic.blocktanks.elementos.Imagen;
import com.vic.blocktanks.io.Entradas;
import com.vic.blocktanks.utilidades.Config;
import com.vic.blocktanks.utilidades.Globales;
import com.vic.blocktanks.utilidades.Recurso;

public class PantallaMapas implements Screen, MenuScreen {

    private Imagen fondo;
    private SpriteBatch batch;
    private ShapeRenderer sr;

    // Botones para selección de mapa
    private Texto opciones[] = new Texto[3];
    private String textos[] = {"Mapa 1", "Mapa 2", "Mapa 3"};
    private Texto test;

    private int opc = 1;
    private boolean mouseArriba = false;
    public float tiempo = 0;

    private Entradas entradas = new Entradas(this);

    @Override
    public void show() {
        fondo = new Imagen(Recurso.FONDOMENU);
        fondo.setSize(Config.ANCHO, Config.ALTO);
        batch = Globales.batch;
        sr = new ShapeRenderer();

        Gdx.input.setInputProcessor(entradas);

        float avance = 50;
        test = new Texto(Recurso.FUENTEMENU, 40, Color.WHITE, true);
        test.setPosition(0, 100);

        for (int i = 0; i < opciones.length; i++) {
            opciones[i] = new Texto(Recurso.FUENTEMENU, 75, Color.WHITE, true);
            opciones[i].setTexto(textos[i]);
            opciones[i].setPosition((Config.ANCHO / 2) - (opciones[i].getAncho() / 2),
                ((Config.ALTO / 2) + (opciones[0].getAlto() / 2)) - ((opciones[0].getAlto() * i) + (avance * i)));
        }
    }

    @Override
    public void render(float delta) {
        Globales.LimpiarPantalla(0, 0, 0);

        batch.begin();
        fondo.dibujar();
        for (int i = 0; i < opciones.length; i++) {
            opciones[i].dibujar();
        }
        test.setTexto("Cord x " + entradas.getMouseX() + " cord y " + entradas.getMouseY());
        test.dibujar();
        batch.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        for (int i = 0; i < opciones.length; i++) {
            sr.rect(opciones[i].getX(), opciones[i].getY() - opciones[i].getAlto(), opciones[i].getAncho(), opciones[i].getAlto());
        }
        sr.end();

        tiempo += delta;
        if (entradas.isAbajo()) {
            if (tiempo > 0.12f) {
                tiempo = 0;
                opc++;
                if (opc > 3) {
                    opc = 1;
                }
            }
        }
        if (entradas.isArriba()) {
            if (tiempo > 0.12f) {
                tiempo = 0;
                opc--;
                if (opc < 1) {
                    opc = 3;
                }
            }
        }
        int cont = 0;
        for (int i = 0; i < opciones.length; i++) {
            if (entradas.getMouseX() >= opciones[i].getX() &&
                entradas.getMouseX() <= (opciones[i].getX() + opciones[i].getAncho()) &&
                entradas.getMouseY() >= opciones[i].getY() - opciones[i].getAlto() &&
                entradas.getMouseY() <= opciones[i].getY()) {
                opc = i + 1;
                cont++;
            }
        }
        mouseArriba = (cont > 0);

        for (int i = 0; i < opciones.length; i++) {
            if (i == (opc - 1)) {
                opciones[i].setColor(Color.YELLOW);
            } else {
                opciones[i].setColor(Color.WHITE);
            }
        }

        if (entradas.isEnter() || entradas.isClick()) {
            if (opc == 1) {
                Globales.app.setScreen(new PantallaJuego("maps/mapa1.tmx"));
            } else if (opc == 2) {
                Globales.app.setScreen(new PantallaJuego("maps/mapa2.tmx"));
            } else if (opc == 3) {
                Globales.app.setScreen(new PantallaJuego("maps/mapa3.tmx"));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // Opcionalmente, actualizar viewport
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() { }

    // Implementación de MenuScreen
    @Override
    public void cambiarOpcion(int direccion) {
        opc += direccion;
        if (opc < 1) opc = 3;
        if (opc > 3) opc = 1;
    }
}
