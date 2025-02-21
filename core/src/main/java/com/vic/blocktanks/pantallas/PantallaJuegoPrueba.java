//package com.vic.blocktanks.pantallas;
//
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.maps.MapLayer;
//import com.badlogic.gdx.maps.MapObject;
//import com.badlogic.gdx.maps.tiled.TiledMap;
//import com.badlogic.gdx.maps.tiled.TmxMapLoader;
//import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
//import com.badlogic.gdx.maps.objects.RectangleMapObject;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.utils.viewport.StretchViewport;
//import com.vic.blocktanks.elementos.BotTank;
//import com.vic.blocktanks.elementos.BotTank.ModoBot;
//import com.vic.blocktanks.elementos.Tank;
//import com.vic.blocktanks.elementos.Bala;
//import com.vic.blocktanks.utilidades.Globales;
//import com.vic.blocktanks.utilidades.Config;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PantallaJuegoPrueba implements Screen {
//    private OrthographicCamera camara;
//    private StretchViewport viewport;
//    private TiledMap mapa;
//    private OrthogonalTiledMapRenderer renderMapa;
//
//    private float mapWorldWidth;
//    private float mapWorldHeight;
//
//    private Tank jugador;
//    private List<Rectangle> obstacles;
//    private List<BotTank> bots;
//
//    private boolean playerDead = false;  // Se activa cuando el jugador pierde todas sus vidas
//
//    @Override
//    public void show() {
//        // Carga del mapa
//        mapa = new TmxMapLoader().load("maps/mapa1.tmx");
//        int mapTilesX = mapa.getProperties().get("width", Integer.class);
//        int mapTilesY = mapa.getProperties().get("height", Integer.class);
//        int tileWidth = mapa.getProperties().get("tilewidth", Integer.class);
//        int tileHeight = mapa.getProperties().get("tileheight", Integer.class);
//        mapWorldWidth = (tileWidth * mapTilesX) / 32f;
//        mapWorldHeight = (tileHeight * mapTilesY) / 32f;
//
//        // Configuración de cámara y viewport
//        camara = new OrthographicCamera();
//        viewport = new StretchViewport(mapWorldWidth, mapWorldHeight, camara);
//        camara.position.set(mapWorldWidth / 2f, mapWorldHeight / 2f, 0);
//        camara.update();
//
//        // Inicialización del renderizador del mapa
//        renderMapa = new OrthogonalTiledMapRenderer(mapa, 1 / 32f);
//
//        // Cargar obstáculos de la capa "colisiones"
//        obstacles = new ArrayList<>();
//        MapLayer obstaclesLayer = mapa.getLayers().get("colisiones");
//        if (obstaclesLayer != null) {
//            for (MapObject obj : obstaclesLayer.getObjects()) {
//                if (obj instanceof RectangleMapObject) {
//                    Rectangle rect = ((RectangleMapObject) obj).getRectangle();
//                    obstacles.add(new Rectangle(rect.x / 32f, rect.y / 32f, rect.width / 32f, rect.height / 32f));
//                }
//            }
//        }
//        System.out.println("Obstacles cargados: " + obstacles.size());
//
//        // Inicializar jugador
//        jugador = new Tank(1f, 1f, 1f, mapWorldHeight / 2f);
//
//        // Inicializar bots (con valores mínimos)
//        bots = new ArrayList<>();
//        BotTank botDerecha = new BotTank(1f, 1f, mapWorldWidth - 2f, mapWorldHeight / 2f, ModoBot.AGRESIVO);
//        Vector2 centroJugador = new Vector2(jugador.x + jugador.ancho / 2, jugador.y + jugador.alto / 2);
//        Vector2 centroBot = new Vector2(botDerecha.x + botDerecha.ancho / 2, botDerecha.y + botDerecha.alto / 2);
//        Vector2 diff = centroJugador.cpy().sub(centroBot);
//        botDerecha.angle = diff.angleDeg();
//        bots.add(botDerecha);
//
//        BotTank botArriba = new BotTank(1f, 1f, mapWorldWidth / 2f, mapWorldHeight - 2f, ModoBot.ORBITANTE);
//        botArriba.x = mapWorldWidth / 2f;
//        botArriba.y = mapWorldHeight - 2.5f;
//        botArriba.angle = 270f;
//        bots.add(botArriba);
//
//        BotTank botAbajo = new BotTank(1f, 1f, mapWorldWidth / 2f, 3.5f, ModoBot.ORBITANTE);
//        botAbajo.angle = 90f;
//        bots.add(botAbajo);
//    }
//
//    // Método que verifica colisiones entre balas y tanques y actualiza vidas
//    private void checkBulletCollisions() {
//        List<Bala> playerBullets = jugador.getBalas();
//        for (Bala bala : playerBullets) {
//            if (!bala.isActive()) continue;
//            for (int i = bots.size() - 1; i >= 0; i--) {
//                BotTank bot = bots.get(i);
//                if (bala.getBounds().overlaps(bot.getBounds())) {
//                    bot.reduceLife();
//                    bala.setActive(false);
//                    if (bot.getLives() <= 0) {
//                        bots.remove(i);
//                    }
//                    break;
//                }
//            }
//        }
//        for (BotTank bot : bots) {
//            List<Bala> botBullets = bot.getBalas();
//            for (Bala bala : botBullets) {
//                if (!bala.isActive()) continue;
//                if (bala.getBounds().overlaps(jugador.getBounds())) {
//                    jugador.reduceLife();
//                    bala.setActive(false);
//                    if (jugador.getLives() <= 0) {
//                        playerDead = true;
//                    }
//                    break;
//                }
//            }
//        }
//    }
//
//    @Override
//    public void render(float delta) {
//        viewport.apply();
//        camara.update();
//
//        // Limpieza de pantalla
//        Globales.LimpiarPantalla(0, 0, 0);
//
//        // Dibujar el mapa
//        renderMapa.setView(camara);
//        renderMapa.render();
//
//        // Dibujar entidades (jugador y bots)
//        Globales.batch.setProjectionMatrix(camara.combined);
//        Globales.batch.begin();
//        if (!playerDead) {
//            jugador.draw();
//        }
//        for (BotTank bot : bots) {
//            bot.draw();
//        }
//        Globales.batch.end();
//
//        // Actualización de la lógica del juego si el jugador sigue vivo
//        if (!playerDead) {
//            float oldJugadorX = jugador.x;
//            float oldJugadorY = jugador.y;
//            jugador.update(delta, obstacles, mapWorldWidth, mapWorldHeight);
//            Vector2 posJugador = new Vector2(jugador.x + jugador.ancho / 2, jugador.y + jugador.alto / 2);
//            for (BotTank bot : bots) {
//                float oldBotX = bot.x;
//                float oldBotY = bot.y;
//                bot.update(delta, obstacles, mapWorldWidth, mapWorldHeight, posJugador, bots, jugador);
//                if (jugador.getBounds().overlaps(bot.getBounds())) {
//                    jugador.x = oldJugadorX;
//                    jugador.y = oldJugadorY;
//                    jugador.updateSprite();
//                    bot.x = oldBotX;
//                    bot.y = oldBotY;
//                    bot.updateSprite();
//                }
//            }
//            checkBulletCollisions();
//        }
//
//        // Transición a GameOver si el jugador ha perdido todas las vidas
//        if (playerDead) {
//            Globales.app.setScreen(new PantallaGameOver());
//        }
//    }
//
//    @Override
//    public void resize(int width, int height) {
//        viewport.update(width, height);
//    }
//
//    @Override public void pause() { }
//    @Override public void resume() { }
//    @Override public void hide() { }
//
//    @Override
//    public void dispose() {
//        mapa.dispose();
//        renderMapa.dispose();
//        jugador.dispose();
//        for (BotTank bot : bots) {
//            bot.dispose();
//        }
//    }
//}
