//package com.vic.blocktanks.io;
//
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.InputProcessor;
//import com.vic.blocktanks.pantallas.PantallaMenu;
//
//public class Entradas implements InputProcessor {
//
//    private boolean abajo = false;
//    private boolean arriba = true;
//    PantallaMenu app;
//
//    public Entradas(PantallaMenu app) {
//        this.app = app;
//    }
//
//    public boolean isAbajo() {
//        return abajo;
//    }
//
//    public boolean isArriba() {
//        return arriba;
//    }
//    @Override
//    public boolean keyDown(int keycode) {
//        app.tiempo = 0;
//
//        if (keycode == Input.Keys.DOWN && !abajo) {
//            abajo = true;
//            app.cambiarOpcion(1); // Llama a un método en PantallaMenu para cambiar la opción
//        }
//
//        if (keycode == Input.Keys.UP && !arriba) {
//            arriba = true;
//            app.cambiarOpcion(-1);
//        }
//
//        return true;
//    }
//
//    @Override
//    public boolean keyUp(int keycode) {
//        if (keycode == Input.Keys.DOWN) {
//            abajo = false;
//        }
//        if (keycode == Input.Keys.UP) {
//            arriba = false;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean keyTyped(char character) {
//        return false;
//    }
//
//    @Override
//    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        return false;
//    }
//
//    @Override
//    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//        return false;
//    }
//
//    @Override
//    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
//        return false;
//    }
//
//    @Override
//    public boolean touchDragged(int screenX, int screenY, int pointer) {
//        return false;
//    }
//
//    @Override
//    public boolean mouseMoved(int screenX, int screenY) {
//        return false;
//    }
//
//    @Override
//    public boolean scrolled(float amountX, float amountY) {
//        return false;
//    }
//}

package com.vic.blocktanks.io;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.vic.blocktanks.pantallas.PantallaMenu;
import com.vic.blocktanks.utilidades.Config;
import com.vic.blocktanks.utilidades.Globales;

public class Entradas implements InputProcessor {

    private boolean abajo = false;
    private boolean arriba = false;
    private boolean enter = false;
    private boolean click = false;
    private int mouseX = 0, mouseY = 0;

    PantallaMenu app;

    public Entradas(PantallaMenu app) {
        this.app = app;
    }

    public boolean isAbajo() {
        return abajo;
    }

    public boolean isArriba() {
        return arriba;
    }

    public boolean isEnter() {
        return enter;
    }

    public boolean isClick() {
        return click;
    }

    @Override
    public boolean keyDown(int keycode) {
        // System.out.println("gika");
        app.tiempo = 0.12f;

        if(keycode == Input.Keys.DOWN) {
            abajo = true;
        } else if(keycode == Input.Keys.UP) {
            arriba = true;
        }
        if (keycode == Input.Keys.ENTER) {
            enter = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.DOWN) {
            abajo = false;
        }

        if(keycode == Input.Keys.UP) {
            arriba = false;
        }

        if(keycode == Input.Keys.ENTER) {
            enter = false;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        click = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        click = false;
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseX = screenX;
        mouseY = Config.ALTO - screenY;
        return false;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
