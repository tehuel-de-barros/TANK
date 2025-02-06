package com.vic.blocktanks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.vic.blocktanks.pantallas.PantallaCarga;
import com.vic.blocktanks.elementos.Tank;
import com.vic.blocktanks.pantallas.PantallaMenu;
import com.vic.blocktanks.utilidades.Globales;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class BlockTanks extends Game {
    Tank tank;

    @Override
    public void create() {
        Globales.app = this;
        Globales.batch = new SpriteBatch();
        this.setScreen(new PantallaMenu());
        //tank = new Tank(100, 100, 60, 60);
    }


    @Override
    public void render() {
        //ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        super.render();
        //Globales.batch.begin();
            //tank.dibujar();
        //Globales.batch.end();
    }

    @Override
    public void dispose() {
        Globales.batch.dispose();
    }
}
