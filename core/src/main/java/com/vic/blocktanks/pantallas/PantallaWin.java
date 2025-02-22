package com.vic.blocktanks.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vic.blocktanks.elementos.Imagen;
import com.vic.blocktanks.utilidades.Config;
import com.vic.blocktanks.utilidades.Globales;
import com.vic.blocktanks.utilidades.Recurso;

public class PantallaWin implements Screen {
    private Imagen winImage;
    private SpriteBatch batch;
    private OrthographicCamera camara;
    private float timer = 0f;
    private final float WAIT_TIME = 3f; // Tiempo que se muestra la pantalla Win

    @Override
    public void show() {
        // Inicializa la cámara y el batch
        camara = new OrthographicCamera();
        camara.setToOrtho(false, Config.ANCHO, Config.ALTO);
        camara.update();
        batch = Globales.batch;
        batch.setProjectionMatrix(camara.combined);

        // Cargar la imagen de Win
        winImage = new Imagen(Recurso.WIN);
        winImage.setSize(Config.ANCHO, Config.ALTO);
    }

    @Override
    public void render(float delta) {
        // Limpia la pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camara.update();
        batch.setProjectionMatrix(camara.combined);

        batch.begin();
        winImage.dibujar();
        batch.end();

        timer += delta;
        // Después de WAIT_TIME segundos, regresa al menú
        if (timer >= WAIT_TIME) {
            Globales.app.setScreen(new PantallaMenu());
        }
    }

    @Override
    public void resize(int width, int height) {
        camara.setToOrtho(false, width, height);
        camara.update();
        batch.setProjectionMatrix(camara.combined);
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() { }
}
