package com.vic.blocktanks.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.vic.blocktanks.elementos.Tank;
import com.vic.blocktanks.utilidades.Globales;
import java.util.ArrayList;
import java.util.List;

public class PantallaJuego implements Screen {
    private OrthographicCamera camara;
    private StretchViewport viewport;
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderMapa;

    // Dimensiones del mapa en "unidades de mundo"
    private float mapWorldWidth;
    private float mapWorldHeight;

    // Instancia del tanque y lista de obstáculos
    private Tank jugador;
    private List<Rectangle> obstacles;

    @Override
    public void show() {
        // Cargar el mapa desde assets/maps/mapa1.tmx
        mapa = new TmxMapLoader().load("maps/mapa1.tmx");

        // Obtener propiedades del mapa (en píxeles)
        int mapTilesX = mapa.getProperties().get("width", Integer.class);
        int mapTilesY = mapa.getProperties().get("height", Integer.class);
        int tileWidth = mapa.getProperties().get("tilewidth", Integer.class);
        int tileHeight = mapa.getProperties().get("tileheight", Integer.class);

        // Convertir a unidades de mundo: suponiendo que 32 píxeles = 1 unidad
        mapWorldWidth = (tileWidth * mapTilesX) / 32f;
        mapWorldHeight = (tileHeight * mapTilesY) / 32f;

        // Crear la cámara y el viewport que estira el contenido para llenar la pantalla
        camara = new OrthographicCamera();
        viewport = new StretchViewport(mapWorldWidth, mapWorldHeight, camara);
        camara.position.set(mapWorldWidth / 2f, mapWorldHeight / 2f, 0); // Cámara centrada en el mapa
        camara.update();

        // Crear el renderizador del mapa con factor de escala (1/32f)
        renderMapa = new OrthogonalTiledMapRenderer(mapa, 1 / 32f);

        // Extraer obstáculos de la capa "Obstacles" y convertir coordenadas de píxeles a unidades
       // obstacles = new ArrayList<>();
        obstacles = new ArrayList<>();
        MapLayer obstaclesLayer = mapa.getLayers().get("colisiones");
        if (obstaclesLayer != null) {
            for (Object obj : obstaclesLayer.getObjects()) {
                if (obj instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) obj).getRectangle();
                    rect.x /= 32f;
                    rect.y /= 32f;
                    rect.width /= 32f;
                    rect.height /= 32f;
                    obstacles.add(rect);
                }
            }
        }

        System.out.println("Cantidad de obstáculos cargados: " + obstacles.size());

        // Crear el tanque del jugador.
        // Usamos 32/32f (1 unidad) para ancho y alto, posicionándolo en el centro del mapa.
       // jugador = new Tank(32 / 32f, 32 / 32f, mapWorldWidth / 2f, mapWorldHeight / 2f);
        jugador = new Tank(32 / 32f, 32 / 32f, 1f, mapWorldHeight / 2f);
        //el mio es el de abajo
        //jugador = new Tank(2f, 2f, 1f, mapWorldHeight / 2f);

    }

    @Override
    public void render(float delta) {
        // La cámara se mantiene fija en el centro del mapa para mostrar la totalidad

        camara.update();
        renderMapa.setView(camara);

        Globales.batch.setProjectionMatrix(camara.combined);
        renderMapa.render();

        // Actualizar el tanque (con límites del mundo)
        jugador.update(delta, obstacles, mapWorldWidth, mapWorldHeight);



        Globales.batch.begin();
        jugador.dibujar();
        Globales.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        mapa.dispose();
        renderMapa.dispose();
        jugador.dispose();
    }
}
