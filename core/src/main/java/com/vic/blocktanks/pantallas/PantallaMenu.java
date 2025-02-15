package com.vic.blocktanks.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.vic.blocktanks.elementos.Texto;
import com.vic.blocktanks.io.Entradas;
import com.vic.blocktanks.utilidades.Config;
import com.vic.blocktanks.elementos.Imagen;
import com.vic.blocktanks.utilidades.Globales;
import com.vic.blocktanks.utilidades.Recurso;
//mport com.vic.blocktanks.pantallas.PantallaJuego; // Add this line

public class PantallaMenu implements Screen {
    Imagen fondo;
    SpriteBatch b;

    Texto opciones[] = new Texto[3];
    String textos[] = {"Jugar", "Personajes", "Salir"};

    Texto test;

    int opc = 1;
    boolean mouseArriba = false;
    public float tiempo = 0;

    ShapeRenderer sr;
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
            opciones[i].setPosition((Config.ANCHO/2) - (opciones[i].getAncho()/2),
                ((Config.ALTO/2) + (opciones[0].getAlto()/2)) - ((opciones[0].getAlto()*i) + (avance*i)));
        }

    }

    @Override
    public void render(float delta) {
        //o1.setColor(Color.YELLOW);
        b.begin();
            fondo.dibujar();
            for (int i = 0; i < opciones.length; i++) {
                opciones[i].dibujar();
            }
            test.setTexto( "Cord x " + entradas.getMouseX() + " cord y " + entradas.getMouseY());
            test.dibujar();
        sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.RED);
            for (int i = 0; i < opciones.length; i++) {
                sr.rect(opciones[i].getX(), opciones[i].getY()-opciones[i].getAlto(), opciones[i].getAncho(), opciones[i].getAlto());
                //opciones[i].dibujar();
            }

        sr.end();
           // fuente.draw(b, "hola", 100, 100);
        b.end();

        tiempo += delta;

        if(entradas.isAbajo()){
            if(tiempo > 0.12f) {
                tiempo = 0;
                opc++;
                if (opc > 3) {
                    opc = 1;
                }
            }
        }

        if(entradas.isArriba()){
            if(tiempo > 0.12f) {
                tiempo = 0;
                opc--;
                if (opc < 1) {
                    opc = 3;
                }
            }
        }
        int cont = 0;
        for (int i = 0; i < opciones.length; i++) {
            if (entradas.getMouseX() >= opciones[i].getX() && (entradas.getMouseX() <= (opciones[i].getX() + opciones[i].getAncho()))) {
                if (entradas.getMouseY() >= opciones[i].getY() - opciones[i].getAlto() && (entradas.getMouseY() <= (opciones[i].getY()))) {
                    //System.out.println("opcion " + (i+1));
                    opc = i+1;
                   // mouseArriba = true;
                    cont++;
                }
            }
        }
        if(cont > 0) {
            mouseArriba = true;
        } else {
            mouseArriba = false;
        }

        for (int i = 0; i < opciones.length; i++) {
            if(i == (opc-1)) {
                opciones[i].setColor(Color.YELLOW);
            } else {
                opciones[i].setColor(Color.WHITE);
            }
        }

        if(entradas.isEnter() || (entradas.isClick())) {
            if (((opc == 1) && (entradas.isEnter())) || ((opc == 1) && (entradas.isClick()) && (mouseArriba))) {
                Globales.app.setScreen(new PantallaJuego());
            } else if(((opc == 3) && (entradas.isEnter())) || ((opc == 3) && (entradas.isClick()) && (mouseArriba))) {
                Gdx.app.exit();
            }
        }



//        if(opc==1) {
//            o2.setColor(Color.WHITE);
//            o3.setColor(Color.WHITE);
//            o1.setColor(Color.YELLOW);
//        } else if(opc==2) {
//            o2.setColor(Color.WHITE);
//            o1.setColor(Color.WHITE);
//            o2.setColor(Color.YELLOW);
//        } else if(opc==3) {
//            o2.setColor(Color.WHITE);
//            o1.setColor(Color.WHITE);
//            o3.setColor(Color.YELLOW);
//        }
       // System.out.println(tiempo);
    }

//    public void cambiarOpcion(int direccion) {
//        opc += direccion;
//        if (opc < 1) opc = 3;
//        if (opc > 3) opc = 1;
//
//        actualizarColores();
//    }
//
//    private void actualizarColores() {
//        o1.setColor(Color.YELLOW);
//        o2.setColor(Color.WHITE);
//        o3.setColor(Color.WHITE);
//
//        if (opc == 1) {
//            o1.setColor(Color.YELLOW);
//        } else if (opc == 2) {
//            o2.setColor(Color.YELLOW);
//        } else if (opc == 3) {
//            o3.setColor(Color.YELLOW);
//        }
//    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
