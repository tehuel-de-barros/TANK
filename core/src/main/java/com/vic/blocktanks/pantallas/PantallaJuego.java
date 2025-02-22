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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.vic.blocktanks.elementos.BotTank;
import com.vic.blocktanks.elementos.BotTank.ModoBot;
import com.vic.blocktanks.elementos.Tank;
import com.vic.blocktanks.elementos.Bala;
import com.vic.blocktanks.utilidades.Globales;
import com.vic.blocktanks.utilidades.Config;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PantallaJuego implements Screen {

    private String currentMap;
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderMapa;
    private OrthographicCamera camara;
    private StretchViewport viewport;

    private float mapWorldWidth;
    private float mapWorldHeight;

    private Tank jugador;
    private List<BotTank> bots;
    private List<Rectangle> obstacles;

    private boolean playerDead = false;
    // Nivel: 0 = mapa1, 1 = mapa2, 2 = mapa3
    private int nivelActual = 0;

    public PantallaJuego(String mapFile) {
        this.currentMap = mapFile;
        if (currentMap.equals("maps/mapa1.tmx")) {
            nivelActual = 0;
        } else if (currentMap.equals("maps/mapa2.tmx")) {
            nivelActual = 1;
        } else if (currentMap.equals("maps/mapa3.tmx")) {
            nivelActual = 2;
        }
    }

    @Override
    public void show() {
        // Cargar el mapa usando currentMap
        mapa = new TmxMapLoader().load(currentMap);
        int mapTilesX = mapa.getProperties().get("width", Integer.class);
        int mapTilesY = mapa.getProperties().get("height", Integer.class);
        int tileWidth = mapa.getProperties().get("tilewidth", Integer.class);
        int tileHeight = mapa.getProperties().get("tileheight", Integer.class);
        mapWorldWidth = (tileWidth * mapTilesX) / 32f;
        mapWorldHeight = (tileHeight * mapTilesY) / 32f;

        camara = new OrthographicCamera();
        viewport = new StretchViewport(mapWorldWidth, mapWorldHeight, camara);
        camara.position.set(mapWorldWidth / 2f, mapWorldHeight / 2f, 0);
        camara.update();

        renderMapa = new OrthogonalTiledMapRenderer(mapa, 1 / 32f);

        obstacles = new ArrayList<>();
        MapLayer obstaclesLayer = mapa.getLayers().get("colisiones");
        if (obstaclesLayer != null) {
            for (MapObject obj : obstaclesLayer.getObjects()) {
                if (obj instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject)obj).getRectangle();
                    obstacles.add(new Rectangle(rect.x / 32f, rect.y / 32f, rect.width / 32f, rect.height / 32f));
                }
            }
        }
        System.out.println("Obstacles loaded: " + obstacles.size());

        bots = new ArrayList<>();
        if (nivelActual == 0) { // MAPA 1
            jugador = new Tank(1f, 1f, 1f, mapWorldHeight / 2f);

            BotTank botDerecha = new BotTank(1f, 1f, mapWorldWidth - 2f, mapWorldHeight / 2f, ModoBot.AGRESIVO);
            Vector2 centroJugador = new Vector2(jugador.x + jugador.ancho / 2, jugador.y + jugador.alto / 2);
            Vector2 centroBotDer = new Vector2(botDerecha.x + botDerecha.ancho / 2, botDerecha.y + botDerecha.alto / 2);
            Vector2 diffDer = centroJugador.cpy().sub(centroBotDer);
            botDerecha.angle = diffDer.angleDeg();
            bots.add(botDerecha);

            BotTank botArriba = new BotTank(1f, 1f, mapWorldWidth / 2f, mapWorldHeight - 2.5f, ModoBot.ORBITANTE);
            botArriba.angle = 270f;
            bots.add(botArriba);

            BotTank botAbajo = new BotTank(1f, 1f, mapWorldWidth / 2f, 3.5f, ModoBot.ORBITANTE);
            botAbajo.angle = 90f;
            bots.add(botAbajo);

        } else if (nivelActual == 1) { // MAPA 2
            jugador = new Tank(1f, 1f, 1f, mapWorldHeight / 2f);

            BotTank botDerecha = new BotTank(1f, 1f, mapWorldWidth - 2f, mapWorldHeight / 2f, ModoBot.AGRESIVO);
            Vector2 centroJugador = new Vector2(jugador.x + jugador.ancho / 2, jugador.y + jugador.alto / 2);
            Vector2 centroBotDer = new Vector2(botDerecha.x + botDerecha.ancho / 2, botDerecha.y + botDerecha.alto / 2);
            Vector2 diffDer = centroJugador.cpy().sub(centroBotDer);
            botDerecha.angle = diffDer.angleDeg();
            bots.add(botDerecha);

            // Bot superior se posiciona más abajo para no quedar dentro de la pared superior
            BotTank botArriba = new BotTank(1f, 1f, mapWorldWidth / 2f, mapWorldHeight - 4f, ModoBot.ORBITANTE);
            botArriba.angle = 270f;
            bots.add(botArriba);

            BotTank botAbajo = new BotTank(1f, 1f, mapWorldWidth / 2f, 3.5f, ModoBot.ORBITANTE);
            botAbajo.angle = 90f;
            bots.add(botAbajo);

            // Ajustar los obstáculos (barriles) para que ocupen solo 1 cuadrado, dejando las paredes intactas.
            for (Rectangle r : obstacles) {
                if (r.y > 1f && (r.y + r.height) < mapWorldHeight - 1f) {
                    r.width = 1f;
                    r.height = 1f;
                }
            }

        } else if (nivelActual == 2) { // MAPA 3
            jugador = new Tank(1f, 1f, 2f, mapWorldHeight / 2f);

            BotTank botDerecha = new BotTank(1f, 1f, mapWorldWidth - 5f, mapWorldHeight / 2f, ModoBot.AGRESIVO);
            Vector2 centroJugador = new Vector2(jugador.x + jugador.ancho / 2, jugador.y + jugador.alto / 2);
            Vector2 centroBotDer = new Vector2(botDerecha.x + botDerecha.ancho / 2, botDerecha.y + botDerecha.alto / 2);
            Vector2 diffDer = centroJugador.cpy().sub(centroBotDer);
            botDerecha.angle = diffDer.angleDeg();
            bots.add(botDerecha);

            BotTank botArriba = new BotTank(1f, 1f, mapWorldWidth / 2f, mapWorldHeight - 4.5f, ModoBot.ORBITANTE);
            botArriba.angle = 270f;
            bots.add(botArriba);

            BotTank botAbajo = new BotTank(1f, 1f, mapWorldWidth / 2f, 3.5f, ModoBot.ORBITANTE);
            botAbajo.angle = 90f;
            bots.add(botAbajo);
        } else {
            jugador = new Tank(1f, 1f, 1f, mapWorldHeight / 2f);
        }
    }

    private void checkBulletCollisions() {
        List<Bala> playerBullets = jugador.getBalas();
        for (Bala bala : playerBullets) {
            if (!bala.isActive()) continue;
            for (int i = bots.size() - 1; i >= 0; i--) {
                BotTank bot = bots.get(i);
                if (bala.getBounds().overlaps(bot.getBounds())) {
                    bot.reduceLife();
                    bala.setActive(false);
                    if (bot.getLives() <= 0) {
                        bots.remove(i);
                    }
                    break;
                }
            }
        }
        for (BotTank bot : bots) {
            List<Bala> botBullets = bot.getBalas();
            for (Bala bala : botBullets) {
                if (!bala.isActive()) continue;
                if (bala.getBounds().overlaps(jugador.getBounds())) {
                    jugador.reduceLife();
                    bala.setActive(false);
                    if (jugador.getLives() <= 0) {
                        playerDead = true;
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        viewport.apply();
        camara.update();
        Globales.LimpiarPantalla(0, 0, 0);

        renderMapa.setView(camara);
        renderMapa.render();

        Globales.batch.setProjectionMatrix(camara.combined);
        Globales.batch.begin();
        if (!playerDead) {
            jugador.draw();
        }
        for (BotTank bot : bots) {
            bot.draw();
        }
        Globales.batch.end();

        if (!playerDead) {
            float oldJugadorX = jugador.x;
            float oldJugadorY = jugador.y;
            jugador.update(delta, obstacles, mapWorldWidth, mapWorldHeight);
            Vector2 posJugador = new Vector2(jugador.x + jugador.ancho / 2, jugador.y + jugador.alto / 2);
            for (BotTank bot : bots) {
                float oldBotX = bot.x;
                float oldBotY = bot.y;
                bot.update(delta, obstacles, mapWorldWidth, mapWorldHeight, posJugador, bots, jugador);
                if (jugador.getBounds().overlaps(bot.getBounds())) {
                    jugador.x = oldJugadorX;
                    jugador.y = oldJugadorY;
                    jugador.updateSprite();
                    bot.x = oldBotX;
                    bot.y = oldBotY;
                    bot.updateSprite();
                }
            }
            checkBulletCollisions();
        }

        if (bots.isEmpty() && !playerDead) {
            String nextMap;
            if (nivelActual == 0) {
                nextMap = "maps/mapa2.tmx";
                nivelActual = 1;
            } else if (nivelActual == 1) {
                nextMap = "maps/mapa3.tmx";
                nivelActual = 2;
            } else if (nivelActual == 2) {
                Globales.app.setScreen(new PantallaWin());
                return;
            } else {
                nextMap = "maps/mapa1.tmx";
                nivelActual = 0;
            }
            Globales.app.setScreen(new PantallaJuego(nextMap));
        }

        if (playerDead) {
            Globales.app.setScreen(new PantallaGameOver());
        }
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
        jugador.dispose();
        for (BotTank bot : bots) {
            bot.dispose();
        }
    }
}
