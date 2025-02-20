//package com.vic.blocktanks.elementos;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.Rectangle;
//
//public class Obstaculo extends Rectangle {
//    private Texture textura;
//
//    public Obstaculo(float x, float y, float width, float height) {
//        super(x, y, width, height);
//        // Se asume que "rock.png" existe en assets.
//        textura = new Texture("rock.png");
//    }
//
//    public void dibujar(SpriteBatch batch) {
//        batch.draw(textura, x, y, width, height);
//    }
//
//    public void dispose() {
//        textura.dispose();
//    }
//}
