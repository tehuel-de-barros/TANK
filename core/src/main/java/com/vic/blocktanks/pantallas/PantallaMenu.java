package com.vic.blocktanks.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.vic.blocktanks.elementos.Imagen;
import com.vic.blocktanks.elementos.Texto;
import com.vic.blocktanks.io.Entradas;
import com.vic.blocktanks.utilidades.Config;
import com.vic.blocktanks.utilidades.Globales;
import com.vic.blocktanks.utilidades.Recurso;

public class PantallaMenu implements Screen, MenuScreen {

    private Imagen fondo;
    private SpriteBatch batch;
    private ShapeRenderer sr;

    private Texto opciones[] = new Texto[4];
    private String textos[] = new String[] { "Jugar", "1v1", "Niveles", "Salir" };
    private Texto test;

    private int opc = 1;
    private int hoveredOption = -1;
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
            opciones[i].setPosition(
                (Config.ANCHO / 2f) - (opciones[i].getAncho() / 2),
                ((Config.ALTO / 2f) + (opciones[0].getAlto() / 2f)) - ((opciones[0].getAlto() * i) + (avance * i))
            );
        }
    }

    @Override
    public void render(float delta) {
        Globales.LimpiarPantalla(0, 0, 0);

        hoveredOption = -1;
        for (int i = 0; i < opciones.length; i++) {
            float ox = opciones[i].getX();
            float oy = opciones[i].getY();
            float ow = opciones[i].getAncho();
            float oh = opciones[i].getAlto();
            int mx = entradas.getMouseX();
            int my = entradas.getMouseY();
            if (mx >= ox && mx <= ox + ow && my >= oy - oh && my <= oy) {
                hoveredOption = i + 1;
                break;
            }
        }

        batch.begin();
        fondo.dibujar();
        for (int i = 0; i < opciones.length; i++) {
            if (hoveredOption != -1) {
                if (i == (hoveredOption - 1)) {
                    opciones[i].setColor(Color.YELLOW);
                } else {
                    opciones[i].setColor(Color.WHITE);
                }
            } else {
                if (i == (opc - 1)) {
                    opciones[i].setColor(Color.YELLOW);
                } else {
                    opciones[i].setColor(Color.WHITE);
                }
            }
            opciones[i].dibujar();
        }
        test.setTexto("Cord x " + entradas.getMouseX() + " cord y " + entradas.getMouseY());
        test.dibujar();
        batch.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        for (int i = 0; i < opciones.length; i++) {
            sr.rect(opciones[i].getX(), opciones[i].getY() - opciones[i].getAlto(),
                opciones[i].getAncho(), opciones[i].getAlto());
        }
        sr.end();

        tiempo += delta;
        if (hoveredOption == -1) {
            if (entradas.isAbajo()) {
                if (tiempo > 0.12f) {
                    tiempo = 0;
                    opc++;
                    if (opc > opciones.length) {
                        opc = 1;
                    }
                }
            }
            if (entradas.isArriba()) {
                if (tiempo > 0.12f) {
                    tiempo = 0;
                    opc--;
                    if (opc < 1) {
                        opc = opciones.length;
                    }
                }
            }
        }

        if (entradas.isEnter() || entradas.isClick()) {
            if (entradas.isEnter()) {
                seleccionarOpcion(opc);
            } else if (entradas.isClick()) {
                if (hoveredOption != -1) {
                    seleccionarOpcion(hoveredOption);
                }
            }
        }
    }

    private void seleccionarOpcion(int opcionSeleccionada) {
        if (opcionSeleccionada == 1) {
            Globales.app.setScreen(new PantallaJuego("maps/mapa1.tmx"));
        } else if (opcionSeleccionada == 2) {
            Globales.app.setScreen(new Pantalla1v1());
        } else if (opcionSeleccionada == 3) {
            Globales.app.setScreen(new PantallaMapas());
        } else if (opcionSeleccionada == 4) {
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() { }

    @Override
    public void cambiarOpcion(int direccion) {
        opc += direccion;
        if (opc < 1) {
            opc = opciones.length;
        }
        if (opc > opciones.length) {
            opc = 1;
        }
    }
}
