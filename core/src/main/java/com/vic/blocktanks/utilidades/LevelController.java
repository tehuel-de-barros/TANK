package com.vic.blocktanks.utilidades;

import java.util.ArrayList;
import java.util.List;

public class LevelController {
    private static List<String> niveles = new ArrayList<>();
    private static int indiceActual = 0;

    static {
        niveles.add("maps/mapa1.tmx");
        niveles.add("maps/mapa2.tmx");
        // Agrega más niveles según necesites
    }

    public static String getCurrentLevel() {
        return niveles.get(indiceActual);
    }

    public static void nextLevel() {
        indiceActual++;
        if (indiceActual >= niveles.size()) {
            // Podés cambiar a una pantalla de victoria o reiniciar
            indiceActual = 0;
        }
    }
}
