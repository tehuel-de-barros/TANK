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

public class PantallaGameOver implements Screen {

    private Imagen gameOver;
    private SpriteBatch batch;
    private OrthographicCamera camara;
    private boolean fadeInCompleted = false, fadeOutCompleted = false;
    private float alpha = 0f;
    private float timeCounter = 0f;
    private final float waitTime = 3.0f; // Tiempo en que la imagen se muestra con alpha=1
    private final float fadeSpeed = 0.02f; // Velocidad de fade in/out

    @Override
    public void show() {
        // Re-inicializa la cámara y establece la proyección
        camara = new OrthographicCamera();
        camara.setToOrtho(false, Config.ANCHO, Config.ALTO);
        camara.update();
        batch = Globales.batch;
        batch.setProjectionMatrix(camara.combined);

        // Carga la imagen Game Over
        gameOver = new Imagen(Recurso.GAMEOVER);
        gameOver.setSize(Config.ANCHO, Config.ALTO);
        gameOver.setTransparencia(alpha);
    }

    @Override
    public void render(float delta) {
        // Limpia la pantalla y actualiza la cámara
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camara.update();
        batch.setProjectionMatrix(camara.combined);

        // Dibuja la imagen Game Over
        batch.begin();
        gameOver.dibujar();
        batch.end();

        // Procesa el efecto de fade
        processFade(delta);
    }

    private void processFade(float delta) {
        if (!fadeInCompleted) {
            alpha += fadeSpeed;
            if (alpha >= 1f) {
                alpha = 1f;
                fadeInCompleted = true;
            }
        } else {
            timeCounter += delta;
            if (timeCounter >= waitTime) {
                alpha -= fadeSpeed;
                if (alpha <= 0f) {
                    alpha = 0f;
                    fadeOutCompleted = true;
                }
            }
        }
        gameOver.setTransparencia(alpha);

        // Cuando finalice el fade out, vuelve al menú
        if (fadeOutCompleted) {
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
