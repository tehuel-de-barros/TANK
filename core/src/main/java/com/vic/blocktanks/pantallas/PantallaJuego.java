package com.vic.blocktanks.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.vic.blocktanks.elementos.BotTank;
import com.vic.blocktanks.elementos.BotTank.ModoBot;
import com.vic.blocktanks.elementos.Tank;
import com.vic.blocktanks.utilidades.Globales;
import java.util.ArrayList;
import java.util.List;

public class PantallaJuego implements Screen {
    private OrthographicCamera camara;
    private StretchViewport viewport;
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderMapa;

    // Dimensiones del mapa en unidades (32 px = 1 unidad)
    private float mapWorldWidth;
    private float mapWorldHeight;

    private Tank jugador;
    private List<Rectangle> obstacles;
    private List<BotTank> bots;

    @Override
    public void show() {
        // Cargar el mapa
        mapa = new TmxMapLoader().load("maps/mapa1.tmx");

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

        // Extraer obstáculos de la capa "colisiones"
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
        System.out.println("Obstáculos cargados: " + obstacles.size());

        // Crear el jugador en el cuadrante inferior izquierdo
        jugador = new Tank(1f, 1f, 1f, mapWorldHeight / 2f);

        // Crear 3 bots
        bots = new ArrayList<>();

        // Bot agresivo: en la misma línea horizontal que el jugador, en el lado derecho.
        BotTank botDerecha = new BotTank(1f, 1f, mapWorldWidth - 2f, mapWorldHeight / 2f, ModoBot.AGRESIVO);
        Vector2 centroJugador = new Vector2(jugador.x + jugador.ancho/2, jugador.y + jugador.alto/2);
        Vector2 centroBot = new Vector2(botDerecha.x + botDerecha.ancho/2, botDerecha.y + botDerecha.alto/2);
        Vector2 diff = centroJugador.cpy().sub(centroBot);
        botDerecha.angle = diff.angleDeg();
        bots.add(botDerecha);

        // Bot orbitante: en la parte superior central
        BotTank botArriba = new BotTank(1f, 1f, mapWorldWidth / 2f, mapWorldHeight - 2f, ModoBot.ORBITANTE);
        // Si el bot aparece muy pegado al borde, ajustamos su posición
        botArriba.x = mapWorldWidth / 2f;
        botArriba.y = mapWorldHeight - 2.5f;
        botArriba.angle = 270f; // Mirando hacia abajo
        bots.add(botArriba);

        // Bot orbitante: en la parte inferior central (ajustado para zona libre)
        BotTank botAbajo = new BotTank(1f, 1f, mapWorldWidth / 2f, 3.5f, ModoBot.ORBITANTE);
        botAbajo.angle = 90f; // Mirando hacia arriba
        bots.add(botAbajo);
    }

    @Override
    public void render(float delta) {
        camara.update();
        renderMapa.setView(camara);

        Globales.batch.setProjectionMatrix(camara.combined);
        renderMapa.render();

        // Actualizar el jugador
        jugador.update(delta, obstacles, mapWorldWidth, mapWorldHeight);
        Vector2 playerPos = new Vector2(jugador.x + jugador.ancho/2, jugador.y + jugador.alto/2);

        // Actualizar bots (verificando colisiones entre ellos y con el jugador)
        for (BotTank bot : bots) {
            bot.update(delta, obstacles, mapWorldWidth, mapWorldHeight, playerPos, bots, jugador);
        }

        Globales.batch.begin();
        jugador.dibujar();
        for (BotTank bot : bots) {
            bot.dibujar();
        }
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
        jugador.dispose();
        for (BotTank bot : bots) {
            bot.dispose();
        }
    }
}
