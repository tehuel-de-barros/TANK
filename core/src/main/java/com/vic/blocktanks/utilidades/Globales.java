package com.vic.blocktanks.utilidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vic.blocktanks.BlockTanks;

public class Globales {
    public static SpriteBatch batch;
    public static BlockTanks app;

    public static void LimpiarPantalla(float r, float g, float b ) {

        Gdx.gl.glClearColor(r, g, b , 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}

