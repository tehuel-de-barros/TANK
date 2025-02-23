package com.vic.blocktanks.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.vic.blocktanks.pantallas.MenuScreen;

public class Entradas implements InputProcessor {
    private boolean abajo = false;
    private boolean arriba = false;
    private boolean enter = false;
    private boolean click = false;
    private int mouseX = 0, mouseY = 0;

    MenuScreen menu;

    public Entradas(MenuScreen menu) {
        this.menu = menu;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP) {
            arriba = true;
        }
        if (keycode == Input.Keys.DOWN) {
            abajo = true;
        }
        if (keycode == Input.Keys.ENTER) {
            enter = true;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.UP) {
            arriba = false;
        }
        if (keycode == Input.Keys.DOWN) {
            abajo = false;
        }
        if (keycode == Input.Keys.ENTER) {
            enter = false;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        click = true;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        click = false;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseX = screenX;
        mouseY = Gdx.graphics.getHeight() - screenY;
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
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

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }
}
