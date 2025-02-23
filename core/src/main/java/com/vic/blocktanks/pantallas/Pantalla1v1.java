package com.vic.blocktanks.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.vic.blocktanks.elementos.Player1;
import com.vic.blocktanks.elementos.Player2;
import com.vic.blocktanks.elementos.Bala;
import com.vic.blocktanks.utilidades.Globales;
import com.vic.blocktanks.utilidades.Config;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;

public class Pantalla1v1 implements Screen {

    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderMapa;
    private OrthographicCamera camara;
    private StretchViewport viewport;

    private float mapWorldWidth;
    private float mapWorldHeight;

    private Player1 player1;
    private Player2 player2;
    private List<Rectangle> obstacles;

    private boolean gameOver = false;

    public Pantalla1v1() {
        // Se usa un mapa para 1v1; por ejemplo, "maps/mapa1.tmx"
        mapa = new TmxMapLoader().load("maps/mapa1.tmx");
        int mapTilesX = mapa.getProperties().get("width", Integer.class);
        int mapTilesY = mapa.getProperties().get("height", Integer.class);
        int tileWidth = mapa.getProperties().get("tilewidth", Integer.class);
        int tileHeight = mapa.getProperties().get("tileheight", Integer.class);
        mapWorldWidth = (tileWidth * mapTilesX) / 32f;
        mapWorldHeight = (tileHeight * mapTilesY) / 32f;

        camara = new OrthographicCamera();
        viewport = new StretchViewport(mapWorldWidth, mapWorldHeight, camara);
        camara.position.set(mapWorldWidth/2f, mapWorldHeight/2f, 0);
        camara.update();

        renderMapa = new OrthogonalTiledMapRenderer(mapa, 1/32f);

        obstacles = new ArrayList<>();
        MapLayer obstaclesLayer = mapa.getLayers().get("colisiones");
        if (obstaclesLayer != null) {
            for (com.badlogic.gdx.maps.MapObject obj : obstaclesLayer.getObjects()) {
                if (obj instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject)obj).getRectangle();
                    obstacles.add(new Rectangle(rect.x/32f, rect.y/32f, rect.width/32f, rect.height/32f));
                }
            }
        }

        // Inicializar jugadores:
        // Player1 usa flechas + SPACE; Player2 usa WASD + F.
        player1 = new Player1(1f, 1f, 1f, mapWorldHeight/2f);
        player2 = new Player2(1f, 1f, mapWorldWidth - 2f, mapWorldHeight/2f);

        // Ambos comienzan con 3 vidas.
        player1.setLives(3);
        player2.setLives(3);
    }

    private void checkBulletCollisions() {
        // Player1 bala vs Player2
        for (Bala bala : player1.getBalas()) {
            if (!bala.isActive()) continue;
            if (bala.getBounds().overlaps(player2.getBounds())) {
                player2.reduceLife();
                bala.setActive(false);
            }
        }
        // Player2 bala vs Player1
        for (Bala bala : player2.getBalas()) {
            if (!bala.isActive()) continue;
            if (bala.getBounds().overlaps(player1.getBounds())) {
                player1.reduceLife();
                bala.setActive(false);
            }
        }
    }

    private void checkGameOver() {
        if (player1.getLives() <= 0 || player2.getLives() <= 0) {
            gameOver = true;
            Globales.app.setScreen(new PantallaMenu());
        }
    }

    @Override
    public void show() {
        // La inicialización se realizó en el constructor
    }

    @Override
    public void render(float delta) {
        viewport.apply();
        camara.update();
        Globales.LimpiarPantalla(0, 0, 0);

        renderMapa.setView(camara);
        renderMapa.render();

        player1.update(delta, obstacles, mapWorldWidth, mapWorldHeight);
        player2.update(delta, obstacles, mapWorldWidth, mapWorldHeight);

        checkBulletCollisions();
        checkGameOver();

        Globales.batch.setProjectionMatrix(camara.combined);
        Globales.batch.begin();
        player1.draw();
        player2.draw();
        Globales.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        mapa.dispose();
        renderMapa.dispose();
        player1.dispose();
        player2.dispose();
    }
}
