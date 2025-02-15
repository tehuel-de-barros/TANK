package com.vic.blocktanks.pantallas;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vic.blocktanks.elementos.Imagen;
import com.vic.blocktanks.utilidades.Config;
import com.vic.blocktanks.utilidades.Globales;
import com.vic.blocktanks.utilidades.Recurso;

public class PantallaCarga implements Screen {

    Imagen carga;
    SpriteBatch b;
    boolean fadeInTerminado = false, termina = false;
    float a = 0;
    float contTiempo = 0, tiempoEspera = 5;
    float contTiempoTermina = 0, tiempoTermina = 5;

    @Override
    public void show() {
        carga = new Imagen(Recurso.CARGA);
        carga.setSize(Config.ANCHO, Config.ALTO);
        b = Globales.batch;
        carga.setTransparencia(a);
    }

    @Override
    public void render(float delta) {
        Globales.LimpiarPantalla(0, 0, 0);
        b.begin();
            carga.dibujar();
        b.end();

        procesarFade();
    }

    private void procesarFade() {
        if(!fadeInTerminado) {
            a += 0.01f;
            if (a > 1) {
                a = 1;
                fadeInTerminado = true;
            }
        } else {
            contTiempo += 0.1f;
            if(contTiempo > tiempoEspera) {
                a -= 0.01f;
                if (a < 0) {
                    a = 0;
                    termina = true;
                }
            }
        }
        carga.setTransparencia(a);

        if(termina) {
            contTiempoTermina += 0.1f;
            if (contTiempoTermina > tiempoTermina) {
                //System.out.println("cambio");
                Globales.app.setScreen(new PantallaMenu());
            }
        }

    }

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
