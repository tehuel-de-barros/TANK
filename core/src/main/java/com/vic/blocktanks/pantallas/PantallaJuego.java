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

    public PantallaJuego(String mapFile) {
        this.currentMap = mapFile;
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

        // Cargar obstáculos desde la capa "colisiones"
        obstacles = new ArrayList<>();
        MapLayer obstaclesLayer = mapa.getLayers().get("colisiones");
        if (obstaclesLayer != null) {
            for (MapObject obj : obstaclesLayer.getObjects()) {
                if (obj instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                    obstacles.add(new Rectangle(rect.x / 32f, rect.y / 32f, rect.width / 32f, rect.height / 32f));
                }
            }
        }
        System.out.println("Obstacles loaded: " + obstacles.size());

        // Inicializar jugador
        // Por defecto se coloca en x = 1f, pero si estamos en mapa3 lo ajustamos para que no esté tan pegado al borde izquierdo
        jugador = new Tank(1f, 1f, 1f, mapWorldHeight / 2f);
        if (currentMap.equals("maps/mapa3.tmx")) {
            jugador.x = 2f; // Ajusta este valor para posicionar al jugador más a la derecha
        }

        // Inicializar bots (ejemplo)
        bots = new ArrayList<>();
        // Bot en la derecha
        BotTank botDerecha = new BotTank(1f, 1f, mapWorldWidth - 2f, mapWorldHeight / 2f, ModoBot.AGRESIVO);
        Vector2 centroJugador = new Vector2(jugador.x + jugador.ancho / 2, jugador.y + jugador.alto / 2);
        Vector2 centroBotDer = new Vector2(botDerecha.x + botDerecha.ancho / 2, botDerecha.y + botDerecha.alto / 2);
        Vector2 diffDer = centroJugador.cpy().sub(centroBotDer);
        botDerecha.angle = diffDer.angleDeg();
        bots.add(botDerecha);

        // Bot en la parte superior
        BotTank botArriba = new BotTank(1f, 1f, mapWorldWidth / 2f, mapWorldHeight - 2f, ModoBot.ORBITANTE);
        botArriba.x = mapWorldWidth / 2f;
        botArriba.y = mapWorldHeight - 2.5f;
        botArriba.angle = 270f;
        bots.add(botArriba);

        // Bot en la parte inferior
        BotTank botAbajo = new BotTank(1f, 1f, mapWorldWidth / 2f, 3.5f, ModoBot.ORBITANTE);
        botAbajo.angle = 90f;
        bots.add(botAbajo);
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

        // Avanzar al siguiente nivel cuando se hayan eliminado todos los bots y el jugador sigue vivo.
        if (bots.isEmpty() && !playerDead) {
            String nextMap;
            if (currentMap.equals("maps/mapa1.tmx")) {
                nextMap = "maps/mapa3.tmx";
            } else if (currentMap.equals("maps/mapa3.tmx")) {
                // Si ya pasó mapa3, mostramos la pantalla Win.
                Globales.app.setScreen(new PantallaWin());
                return;
            } else {
                nextMap = "maps/mapa1.tmx";
            }
            Globales.app.setScreen(new PantallaJuego(nextMap));
        }

        // Transición a Game Over si el jugador muere
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
