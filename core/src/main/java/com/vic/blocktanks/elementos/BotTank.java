//package com.vic.blocktanks.elementos;
//
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.math.Vector2;
//import java.util.List;
//import java.util.Random;
//
//public class BotTank extends Tank {
//    public enum ModoBot {
//        AGRESIVO,
//        ORBITANTE
//    }
//
//    private ModoBot modo;
//    private float timerDisparo = 0f;
//    private float intervaloDisparo = 1.5f; // Dispara cada 1.5 segundos en modo agresivo
//    private Random random;
//
//    // Variables para el modo ORBITANTE
//    private Vector2 centroOrbita;
//    private float radioOrbita;
//    private float anguloOrbita;
//
//    public BotTank(float ancho, float alto, float x, float y, ModoBot modo) {
//        super(ancho, alto, x, y);
//        this.modo = modo;
//        random = new Random();
//
//        if (modo == ModoBot.ORBITANTE) {
//            centroOrbita = new Vector2(x, y);
//            radioOrbita = 3f;
//            anguloOrbita = random.nextFloat() * 360f;
//        }
//    }
//
//    public void update(float delta, List<Rectangle> obstacles, float worldWidth, float worldHeight, Vector2 playerPos) {
//        if (modo == ModoBot.AGRESIVO) {
//            moverHaciaJugador(delta, playerPos);
//            timerDisparo += delta;
//            if (timerDisparo >= intervaloDisparo) {
//                disparar();
//                timerDisparo = 0f;
//            }
//        } else if (modo == ModoBot.ORBITANTE) {
//            if (seAcercaAlJugador(playerPos)) {
//                modo = ModoBot.AGRESIVO;
//                timerDisparo = 0f;
//            } else {
//                orbitarZona(delta);
//            }
//        }
//
//        // Limitar la posición dentro del mapa
//        x = Math.max(0, Math.min(x, worldWidth - ancho));
//        y = Math.max(0, Math.min(y, worldHeight - alto));
//
//        // 🔥 Corregido: Actualizamos la posición y rotación manualmente en lugar de llamar a updateSprite()
//        spr.setPosition(x, y);
//        spr.setRotation(angle - 90);
//
//        actualizarBalas(delta, obstacles);
//    }
//
//    private void moverHaciaJugador(float delta, Vector2 playerPos) {
//        Vector2 direccion = new Vector2(playerPos).sub(x + ancho / 2, y + alto / 2);
//        if (direccion.len() > 0) {
//            direccion.nor();
//            x += direccion.x * speed * delta;
//            y += direccion.y * speed * delta;
//            angle = direccion.angleDeg();
//        }
//    }
//
//    private boolean seAcercaAlJugador(Vector2 playerPos) {
//        return new Vector2(x + ancho / 2, y + alto / 2).dst(playerPos) < 5f;
//    }
//
//    private void orbitarZona(float delta) {
//        anguloOrbita += 30f * delta;
//        if (anguloOrbita >= 360f) anguloOrbita -= 360f;
//
//        float rad = (float) Math.toRadians(anguloOrbita);
//        x = centroOrbita.x + radioOrbita * (float) Math.cos(rad) - ancho / 2;
//        y = centroOrbita.y + radioOrbita * (float) Math.sin(rad) - alto / 2;
//        angle = anguloOrbita;
//    }
//
//    private void actualizarBalas(float delta, List<Rectangle> obstacles) {
//        balas.removeIf(b -> !b.isActive());
//        for (Bala b : balas) {
//            b.update(delta, obstacles);
//        }
//    }
//}
