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

public class PantallaMenu implements Screen {
    Imagen fondo;
    SpriteBatch b;
    ShapeRenderer sr;

    Texto opciones[] = new Texto[3];
    String textos[] = {"Jugar", "Personajes", "Salir"};
    Texto test;

    int opc = 1;
    boolean mouseArriba = false;
    public float tiempo = 0;

    Entradas entradas = new Entradas(this);

    @Override
    public void show() {
        fondo = new Imagen(Recurso.FONDOMENU);
        fondo.setSize(Config.ANCHO, Config.ALTO);
        b = Globales.batch;
        sr = new ShapeRenderer();

        Gdx.input.setInputProcessor(entradas);

        float avance = 50;
        test = new Texto(Recurso.FUENTEMENU, 40 , Color.WHITE, true);
        test.setPosition(0 , 100);

        for (int i = 0; i < opciones.length; i++) {
            opciones[i] = new Texto(Recurso.FUENTEMENU, 75, Color.WHITE, true);
            opciones[i].setTexto(textos[i]);
            opciones[i].setPosition(
                (Config.ANCHO/2) - (opciones[i].getAncho()/2),
                ((Config.ALTO/2) + (opciones[0].getAlto()/2))
                    - ((opciones[0].getAlto()*i) + (avance*i))
            );
        }
    }

    @Override
    public void render(float delta) {
        // Limpieza de pantalla
        Globales.LimpiarPantalla(0, 0, 0);

        // 1) Dibujamos todo con SpriteBatch
        b.begin();
        fondo.dibujar();
        for (int i = 0; i < opciones.length; i++) {
            opciones[i].dibujar();
        }
        test.setTexto("Cord x " + entradas.getMouseX() + " cord y " + entradas.getMouseY());
        test.dibujar();
        b.end();

        // 2) Dibujamos rectángulos con ShapeRenderer
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        for (int i = 0; i < opciones.length; i++) {
            sr.rect(opciones[i].getX(),
                opciones[i].getY() - opciones[i].getAlto(),
                opciones[i].getAncho(),
                opciones[i].getAlto());
        }
        sr.end();

        // Lógica de menú (teclas, mouse, etc.)
        tiempo += delta;
        if(entradas.isAbajo()) {
            if(tiempo > 0.12f) {
                tiempo = 0;
                opc++;
                if (opc > 3) {
                    opc = 1;
                }
            }
        }
        if(entradas.isArriba()) {
            if(tiempo > 0.12f) {
                tiempo = 0;
                opc--;
                if (opc < 1) {
                    opc = 3;
                }
            }
        }
        // Detección de hover con mouse
        int cont = 0;
        for (int i = 0; i < opciones.length; i++) {
            if (entradas.getMouseX() >= opciones[i].getX() &&
                entradas.getMouseX() <= (opciones[i].getX() + opciones[i].getAncho()) &&
                entradas.getMouseY() >= opciones[i].getY() - opciones[i].getAlto() &&
                entradas.getMouseY() <= (opciones[i].getY())) {
                opc = i+1;
                cont++;
            }
        }
        mouseArriba = (cont > 0);

        // Cambiar color de la opción seleccionada
        for (int i = 0; i < opciones.length; i++) {
            if(i == (opc-1)) {
                opciones[i].setColor(Color.YELLOW);
            } else {
                opciones[i].setColor(Color.WHITE);
            }
        }

        // Selección de opción con Enter o click
        if(entradas.isEnter() || entradas.isClick()) {
            if((opc == 1) && (mouseArriba || entradas.isEnter())) {
                Globales.app.setScreen(new PantallaJuego());
            } else if((opc == 3) && (mouseArriba || entradas.isEnter())) {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }
}
